package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.compose.runtime.Stable
import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.ui.model.ChatCard

class ChatListContract {
    sealed class Effect: UiEffect {
        data class ShowError(val errorText: String): Effect()

        sealed class Navigation: Effect() {

        }
    }

    sealed class Event: UiEvent {
        data class SelectChat(val chat: ChatCard) : Event()
        data class LoadChatImage(val chat: ChatCard) : Event()
    }

    @Stable
    data class State(
        override val isLoading: Boolean,
        val chatList: List<ChatCard>,
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