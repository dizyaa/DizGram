package dev.dizyaa.dizgram.feature.chatlist.ui

import dev.dizyaa.dizgram.core.uihelpers.StateViewModel
import dev.dizyaa.dizgram.feature.chatlist.data.ChatRepository

class ChatListViewModel(
    chatRepository: ChatRepository,
) : StateViewModel<ChatListContract.State, ChatListContract.Effect>() {



    override fun setInitialState() = ChatListContract.State.Empty

    override fun onError(exception: Exception) {
        setEffect { ChatListContract.Effect.ShowError(exception.message ?: "Error") }
    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }

    fun selectChat(chatUi: ChatUi) {

    }
}

