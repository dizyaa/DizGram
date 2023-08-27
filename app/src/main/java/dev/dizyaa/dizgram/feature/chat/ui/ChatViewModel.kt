package dev.dizyaa.dizgram.feature.chat.ui

import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chat.data.ChatRepository
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import dev.dizyaa.dizgram.feature.user.domain.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChatViewModel(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
) : StateViewModel<ChatContract.State, ChatContract.Event, ChatContract.Effect>() {

    private val currentUser = CompletableDeferred<User>()

    init {
        initUser()
        initChat()
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
        messages = emptyList(),
    )

    private fun Message.toUi(user: User): MessageCard {
        return MessageCard(
            id = this.id,
            senderId = this.sender.senderId,
            content = this.content,
            sendingStatus = this.status,
            authorName = null,
            fromMe = user.userId == this.sender.senderId,
            date = this.date,
        )
    }

    private fun subscribeMessages() {
        chatRepository.newMessages
            .onEach {
                addNewMessage(it)
            }
            .launchIn(viewModelScope)
    }

    private fun addNewMessage(message: Message) {
        makeRequest {
            val user = currentUser.await()
            val messages = listOf(message.toUi(user)) + state.value.messages

            setState { copy(messages = messages) }
        }
    }

    private fun addToEndOfListMessages(messageList: List<Message>) {
        makeRequest {
            val user = currentUser.await()
            val messages = state.value.messages + messageList.map { it.toUi(user) }

            setState { copy(messages = messages) }
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

    private fun loadMoreMessagesRequired() {
        val from = state.value.messages.lastOrNull() ?: return
        loadMessages(from.id)
    }

    private fun initUser() {
        makeRequest {
            currentUser.complete(userRepository.getCurrentUser())
        }
    }

    companion object {
        private const val CHAT_HISTORY_LIMIT = 30
    }
}
