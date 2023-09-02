package dev.dizyaa.dizgram.feature.chat.ui

import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.downloader.FileDownloadManager
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.core.utils.mapFirst
import dev.dizyaa.dizgram.feature.chat.data.ChatRepository
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageContent
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCardType
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import dev.dizyaa.dizgram.feature.user.domain.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.LinkedList

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val fileDownloadManager: FileDownloadManager,
) : StateViewModel<ChatContract.State, ChatContract.Event, ChatContract.Effect>() {

    private val currentUser = CompletableDeferred<User>()

    private val fileIdToMessageIdMap = mutableMapOf<FileId, MessageId>()

    init {
        initUser()
        initChat()

        subscribeDownloads()
        subscribeMessages()
    }

    override fun handleEvents(event: ChatContract.Event) {
        when (event) {
            is ChatContract.Event.NextPageRequired -> loadMoreMessagesRequired()
        }
    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    override fun onError(exception: Exception) = Unit

    override fun setInitialState() = ChatContract.State(
        isLoading = false,
        chatImage = null,
        chatTitle = "",
        messages = LinkedList(),
    )

    private fun Message.toUi(
        user: User,
        files: List<File>? = null,
        text: String? = null,
    ): MessageCard {
        return MessageCard(
            id = this.id,
            senderId = this.sender.senderId,
            sendingStatus = this.status,
            authorName = null,
            fromMe = user.userId == this.sender.senderId,
            date = this.date,
            type = when (val content = this.content) {
                is MessageContent.Photo -> MessageCardType.WithMedia(
                    text = text ?: content.text,
                    files = files ?: listOf(content.file),
                )
                is MessageContent.Text -> MessageCardType.WithMedia(
                    text = text ?: content.text,
                    files = files ?: emptyList(),
                )
                is MessageContent.Unsupported -> MessageCardType.Unsupported
            }
        )
    }

    private fun subscribeMessages() {
        chatRepository.newMessages
            .onEach {
                addToStartOfListMessage(it)
            }
            .launchIn(viewModelScope)
    }

    private fun addToStartOfListMessage(message: Message) {
        makeRequest {
            val user = currentUser.await()
            val card = message.toUi(user)
            val messages = state.value.messages.toMutableList().apply {
                add(0, card)
            }

            loadImagesFromMessage(card)

            setState { copy(messages = messages) }
        }
    }

    private fun addToEndOfListMessages(messageList: List<Message>) {
        makeRequest {
            val cardList = messageList
                .distinctBy { it.id }
                .groupBy { it.albumId }
                .flatMap { (key, list) ->
                    if (key == null) {
                        val user = currentUser.await()
                        list.map {
                            it.toUi(user)
                        }
                    } else {
                        val card = mergeMessagesToCard(list)
                        if (card != null) listOf(card) else emptyList()
                    }
                }

            val messages: List<MessageCard> = state.value.messages + cardList

            cardList.forEach { loadImagesFromMessage(it) }

            setState { copy(messages = messages) }
        }
    }

    private fun loadImagesFromMessage(message: MessageCard) {
        makeRequest {
            val files = (message.type as? MessageCardType.WithMedia)?.files ?: return@makeRequest

            files.forEach { file ->
                if (file.needToDownload) {
                    fileIdToMessageIdMap[file.id] = message.id
                    fileDownloadManager.download(file.id)
                }
            }
        }
    }

    private fun initChat() {
        loadMessages(MessageId(0L))

        makeRequest {
            chatRepository.getChat().let { chat ->
                setState {
                    copy(
                        chatTitle = chat.name,
                        chatImage = chat.chatPhoto
                    )
                }
            }
        }
    }

    private fun loadMessages(from: MessageId) {
        makeRequest {
            chatRepository.getChatMessages(
                fromMessage = from,
                limit = CHAT_HISTORY_LIMIT,
                offset = 0,
            ).let {
                addToEndOfListMessages(it)
            }
        }
    }

    private fun subscribeDownloads() {
        makeRequest {
            fileDownloadManager
                .downloadedFlow
                .onEach { file ->
                    if (!file.needToDownload) {
                        val fileId = file.id
                        val messageId = fileIdToMessageIdMap[fileId]

                        // TODO: refactoring
                        val messages = state.value.messages.map { card ->
                            val fileList = (card.type as? MessageCardType.WithMedia)?.files ?: return@map card

                            if (card.id == messageId) {
                                card.copy(
                                    type = card.type.copy(
                                        files = fileList.map { fileInternal ->
                                            if (fileInternal.id == fileId) {
                                                file
                                            } else {
                                                fileInternal
                                            }
                                        }
                                    )
                                )
                            } else {
                                card
                            }
                        }



                        setState { copy(messages = messages) }
                    }
                }
                .launchIn(viewModelScope)
        }
    }

    private fun loadMoreMessagesRequired() {
        val from = state.value.messages.lastOrNull() ?: return
        loadMessages(from.id)
    }

    private fun initUser() {
        makeRequest {
            currentUser.complete(userRepository.getCurrentUser())
        }
    }

    private suspend fun mergeMessagesToCard(messages: List<Message>): MessageCard? {
        val user = currentUser.await()

        if (messages.isEmpty()) return null

        if (messages.size == 1) {
            return messages.firstOrNull()
                ?.toUi(user)
        }

        val files = messages.mapNotNull {
            when (val content = it.content) {
                is MessageContent.Photo -> content.file
                else -> null
            }
        }

        val text = messages.mapFirst { message ->
            when (val content = message.content) {
                is MessageContent.Photo -> content.text.takeIf { it.isNotEmpty() }
                is MessageContent.Text -> content.text.takeIf { it.isNotEmpty() }
                else -> null
            }
        } ?: return null

        return messages.firstOrNull()?.toUi(user, files, text)
    }

    companion object {
        private const val CHAT_HISTORY_LIMIT = 30
    }
}
