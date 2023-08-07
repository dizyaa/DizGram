package dev.dizyaa.dizgram.feature.chat.ui

import dev.dizyaa.dizgram.core.uihelpers.StateViewModel

class ChatViewModel(

) : StateViewModel<ChatContract.State, ChatContract.Event, ChatContract.Effect>() {
    override fun setInitialState() = ChatContract.State(
        isLoading = false
    )

    override fun handleEvents(event: ChatContract.Event) {
        when (event) {
            else -> Unit
        }
    }

    override fun onError(exception: Exception) {

    }

    override fun onLoading(loading: Boolean) {
        setState { copy(isLoading = loading) }
    }
}