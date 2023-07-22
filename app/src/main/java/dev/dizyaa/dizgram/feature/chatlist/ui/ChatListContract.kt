package dev.dizyaa.dizgram.feature.chatlist.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter

class ChatListContract {
    sealed class Effect: UiEffect {
        data class ShowError(val errorText: String): Effect()

        sealed class Navigation: Effect() {

        }
    }

    sealed class Event: UiEvent {
        data class SelectChat(val chat: Chat) : Event()
    }

    data class State(
        override val isLoading: Boolean,
        val chatList: List<Chat>,
        val chatFilterList: List<ChatFilter>,
    ): UiState {
        companion object {
            val Empty = State(
                chatList = emptyList(),
                isLoading = false,
                chatFilterList = emptyList(),
            )
        }
    }
}