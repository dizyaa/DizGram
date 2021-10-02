package dev.dizyaa.dizgram.feature.chatlist.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiState

class ChatListContract {
    sealed class Effect: UiEffect {
        data class ShowError(val errorText: String): Effect()

        sealed class Navigation: Effect() {

        }
    }

    data class State(
        override val isLoading: Boolean,
        val chatList: List<ChatUi>,
    ): UiState {
        companion object {
            val Empty = State(
                chatList = emptyList(),
                isLoading = false,
            )
        }
    }
}