package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.lifecycle.viewModelScope
import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chatlist.data.ChatRepository
import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ChatListViewModel(
    private val chatRepository: ChatRepository,
) : StateViewModel<ChatListContract.State, ChatListContract.Event, ChatListContract.Effect>() {

    private val bufferOfUpdates: MutableMap<ChatId, ChatUpdate> = mutableMapOf()

    init {
        loadChats(ChatFilter.Main)

        makeRequest {
            chatRepository
                .chatFilterFlow
                .onEach { list ->
                    setState { copy(chatFilterList = list) }
                }
                .launchIn(viewModelScope)
        }

        makeRequest {
            chatRepository
                .chatsFlow
                .onEach { chat ->
                    setState { copy(chatList = chatList + chat) }

                    bufferOfUpdates.remove(chat.id)?.let {
                        updateChat(it)
                    }
                }
                .launchIn(viewModelScope)
        }

        makeRequest {
            chatRepository
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

    private fun loadChatImage(chat: Chat) {
        makeRequest {
            chat.chatPhoto?.small?.let {
                if (it.needToDownload) {
                    chatRepository.loadPhotoByFileId(chat.id, it.id)
                }
            }
        }
    }

    private fun updateChat(update: ChatUpdate) {
        val chatList = state.value.chatList.toMutableList()
        val index = chatList.indexOfFirst { update.chatId == it.id }

        if (index == -1) {
            bufferOfUpdates.put(update.chatId, update)
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
            chatRepository.loadChatsByFilter(filter)
        }
    }

    private fun selectChat(chat: Chat) {

    }
}

