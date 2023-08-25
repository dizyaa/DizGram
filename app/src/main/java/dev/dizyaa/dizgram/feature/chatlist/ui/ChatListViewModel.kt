package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.isUser
import dev.dizyaa.dizgram.feature.chatlist.data.ChatListRepository
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import dev.dizyaa.dizgram.feature.chatlist.ui.model.ChatCard
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import dev.dizyaa.dizgram.feature.user.domain.User
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber

class ChatListViewModel(
    private val chatListRepository: ChatListRepository,
    private val userRepository: UserRepository,
) : StateViewModel<ChatListContract.State, ChatListContract.Event, ChatListContract.Effect>() {

    private val bufferOfUpdates: MutableMap<ChatId, ChatUpdate> = mutableMapOf()
    private val bufferMutex = Mutex()

    private val currentUser = CompletableDeferred<User>()

    init {
        loadChats(ChatFilter.Main)

        makeRequest {
            currentUser.complete(userRepository.getCurrentUser())
        }

        makeRequest {
            chatListRepository
                .chatFilterFlow
                .onEach { list ->
                    setState { copy(chatFilterList = list) }
                }
                .launchIn(viewModelScope)
        }

        makeRequest {
            chatListRepository
                .chatsFlow
                .onEach { chat ->
                    val user = currentUser.await()
                    setState { copy(chatList = chatList + chat.toCardUi(user)) }

                    bufferMutex.withLock {
                        bufferOfUpdates.remove(chat.id)?.let {
                            updateChat(it)
                        }
                    }
                }
                .launchIn(viewModelScope)
        }

        makeRequest {
            chatListRepository
                .chatUpdatesFlow
                .onEach { update ->
                    updateChat(update)
                }
                .launchIn(viewModelScope)
        }
    }

    override fun setInitialState() = ChatListContract.State.Empty

    override fun handleEvents(event: ChatListContract.Event) {
        when (event) {
            is ChatListContract.Event.SelectChat -> selectChat(event.chat)
            is ChatListContract.Event.LoadChatImage -> loadChatImage(event.chat)
        }
    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    override fun onError(exception: Exception) {
        setEffect { ChatListContract.Effect.ShowError(exception.message ?: "Error") }
    }

    private fun loadChatImage(chat: ChatCard) {
        makeRequest {
            chat.chatPhoto?.small?.let {
                if (it.needToDownload) {
                    chatListRepository.loadPhotoByFileId(chat.id, it.id)
                }
            }
        }
    }

    private suspend fun updateChat(update: ChatUpdate) {
        val chatList = state.value.chatList.toMutableList()
        val index = chatList.indexOfFirst { update.chatId == it.id }

        Timber.d("$index -> $update")

        if (index == -1) {
            bufferMutex.withLock {
                bufferOfUpdates[update.chatId] = update
            }
            return
        }

        when (update) {
            is ChatUpdate.ChatPhoto -> {
                chatList[index] = chatList[index].copy(chatPhoto = update.chatPhoto)
            }
            is ChatUpdate.LastMessage -> {
                chatList[index] = chatList[index].copy(lastMessage = update.message)
            }
            is ChatUpdate.Photo -> {
                val chatPhoto = chatList[index].chatPhoto ?: return

                if (chatPhoto.small?.id == update.photo.id) {
                    chatList[index] = chatList[index].copy(
                        chatPhoto = chatPhoto.copy(
                            small = update.photo
                        )
                    )
                }

                if (chatPhoto.big?.id == update.photo.id) {
                    chatList[index] = chatList[index].copy(
                        chatPhoto = chatPhoto.copy(
                            big = update.photo
                        )
                    )
                }
            }
        }

        setState { copy(chatList = chatList) }
    }

    private fun loadChats(filter: ChatFilter) {
        makeRequest {
            chatListRepository.loadChatsByFilter(filter)
        }
    }

    private fun selectChat(chat: ChatCard) {
        setEffect { ChatListContract.Effect.Navigation.ChatRequired(chatId = chat.id) }
    }

    private fun Chat.toCardUi(user: User): ChatCard {
        val senderId = this.lastMessage?.sender?.senderId
        return ChatCard(
            id = id,
            lastMessage = lastMessage,
            name = name,
            chatPhoto = chatPhoto,
            lastMessageFromMyself = (senderId?.isUser() == true) && (senderId == user.userId)
        )
    }
}

