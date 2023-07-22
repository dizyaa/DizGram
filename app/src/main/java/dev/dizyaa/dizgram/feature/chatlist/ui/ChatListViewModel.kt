package dev.dizyaa.dizgram.feature.chatlist.ui

import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chatlist.data.ChatRepository
import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter

class ChatListViewModel(
    private val chatRepository: ChatRepository,
) : StateViewModel<ChatListContract.State, ChatListContract.Event, ChatListContract.Effect>() {

    init {
        loadChats(ChatFilter.Main)

        makeRequest {
            chatRepository
                .chatFilterFlow
                .collect { list ->
                    setState { copy(chatFilterList = list) }
                }
        }

        makeRequest {
            chatRepository
                .chatFlow
                .collect { chat ->
                    setState { copy(chatList = chatList + chat) }
                }
        }
    }

    override fun setInitialState() = ChatListContract.State.Empty

    override fun handleEvents(event: ChatListContract.Event) {
        when (event) {
            is ChatListContract.Event.SelectChat -> selectChat(event.chat)
        }
    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    override fun onError(exception: Exception) {
        setEffect { ChatListContract.Effect.ShowError(exception.message ?: "Error") }
    }

    private fun loadChats(filter: ChatFilter) {
        makeRequest {
            setState { copy(chatList = emptyList()) }
            chatRepository.loadChatsByFilter(filter)
        }
    }

    private fun selectChat(chat: Chat) {

    }
}

