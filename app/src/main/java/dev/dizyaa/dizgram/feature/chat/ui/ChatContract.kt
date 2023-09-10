package dev.dizyaa.dizgram.feature.chat.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard

class ChatContract {

    data class State(
        val messages: List<MessageCard>,
        val chatTitle: String,
        val chatImage: ChatPhoto?,
        val inputTextMessage: String,
        val canSendMessage: Boolean,
        override val isLoading: Boolean,
    ): UiState {
        companion object {
            fun mock() = State(
                messages = (0..19).map {
                    MessageCard.mock(it.toLong(), it % 2 == 0)
                },
                chatTitle = "Chat )",
                chatImage = null,
                isLoading = false,
                inputTextMessage = "",
                canSendMessage = true,
            )
        }
    }

    sealed class Event: UiEvent {
        object NextPageRequired : Event()
        data class ChangeInputTextMessage(val text: String) : Event()
    }

    sealed class Effect: UiEffect {

        sealed class Navigation: Effect()
    }
}