package dev.dizyaa.dizgram.feature.chat.ui

import dev.dizyaa.dizgram.core.uihelpers.UiEffect
import dev.dizyaa.dizgram.core.uihelpers.UiEvent
import dev.dizyaa.dizgram.core.uihelpers.UiState
import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.SizedPhoto
import dev.dizyaa.dizgram.feature.chat.domain.VoiceNote
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.chat.ui.model.VoiceNotePlayerState

class ChatContract {

    data class State(
        override val isLoading: Boolean,
        val messages: List<MessageCard>,
        val chatTitle: String,
        val chatImage: SizedPhoto?,
        val inputTextMessage: InputMessage?,
        val canSendMessage: Boolean,
        val voiceNotePlayerState: VoiceNotePlayerState,
    ): UiState {
        companion object {
            fun mock() = State(
                messages = (0..19).map {
                    MessageCard.mock(it.toLong(), it % 2 == 0)
                },
                chatTitle = "Chat )",
                chatImage = null,
                isLoading = false,
                inputTextMessage = null,
                canSendMessage = true,
                voiceNotePlayerState = VoiceNotePlayerState.Empty,
            )
        }
    }

    sealed class Event: UiEvent {
        data class PlayVoiceNoteClick(val messageId: MessageId, val voiceNote: VoiceNote) : Event()
        data class PauseVoiceNoteClick(val messageId: MessageId, val voiceNote: VoiceNote) : Event()
        data class DownloadVoiceNoteClick(val messageId: MessageId, val voiceNote: VoiceNote) : Event()
        data class StopDownloadVoiceNoteClick(val messageId: MessageId, val voiceNote: VoiceNote) : Event()
        object SendMessageClick : Event()
        object NextPageRequired : Event()
        data class ChangeInputTextMessage(val text: String) : Event()
    }

    sealed class Effect: UiEffect {
        sealed class Navigation: Effect()
    }
}