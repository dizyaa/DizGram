package dev.dizyaa.dizgram.feature.chat.ui.model

import dev.dizyaa.dizgram.feature.chat.domain.AlbumMediaId
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.SenderId
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.chat.domain.VoiceNote
import dev.dizyaa.dizgram.feature.user.domain.UserId

data class MessageCard(
    val id: MessageId,
    val senderId: SenderId,
    val sendingStatus: SendingStatus,
    val authorName: String?,
    val fromMe: Boolean,
    val date: Int,
    val text: String,
    val files: List<File>,
    val voiceNote: VoiceNote?,
    val albumMediaId: AlbumMediaId?,
    val type: MessageCardType,
    val playingStatus: PlayingStatus,
    val progress: Progress,
) {
    companion object {
        fun mock(id: Long = -1L, fromMe: Boolean = true) = MessageCard(
            id = MessageId(id),
            senderId = UserId(id),
            sendingStatus = SendingStatus.InProgress,
            authorName = null,
            fromMe = fromMe,
            date = 0,
            albumMediaId = AlbumMediaId(id),
            files = emptyList(),
            text = "Message text",
            type = MessageCardType.TextWithMedia,
            voiceNote = null,
            playingStatus = PlayingStatus.Stop,
            progress = Progress(0f),
        )
    }
}

enum class PlayingStatus {
    Playing, Stop, Pause, Downloading, NeedDownload,
}

val PlayingStatus.isDownloading: Boolean
    get() = this == PlayingStatus.Downloading

val PlayingStatus.isPlaying: Boolean
    get() = this == PlayingStatus.Playing

enum class MessageCardType {
    TextWithMedia,
    Unsupported
}

