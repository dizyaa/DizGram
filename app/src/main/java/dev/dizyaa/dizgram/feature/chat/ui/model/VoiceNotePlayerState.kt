package dev.dizyaa.dizgram.feature.chat.ui.model

import dev.dizyaa.dizgram.feature.chat.domain.MessageId

data class VoiceNotePlayerState(
    val messageId: MessageId?,
    val file: String?,
    val isPlaying: Boolean
) {
    companion object {
        val Empty = VoiceNotePlayerState(
            messageId = null,
            isPlaying = false,
            file = null
        )
    }
}