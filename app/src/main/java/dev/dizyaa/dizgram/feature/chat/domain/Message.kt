package dev.dizyaa.dizgram.feature.chat.domain

import androidx.compose.runtime.Stable
import dev.dizyaa.dizgram.feature.user.domain.UserId

@Stable
data class Message(
    val id: MessageId,
    val sender: MessageSender,
    val chatId: ChatId,
    val content: MessageContent,
    val isPinned: Boolean,
    val isEdited: Boolean,
    val status: SendingStatus,
    val albumId: AlbumMediaId?,
    val date: Int,
) {
    companion object {
        fun mock(id: Long) = Message(
            id = MessageId(id),
            chatId = ChatId(id),
            sender = MessageSender(UserId(id)),
            content = MessageContent.Text(
                "Is this a message? No, this is Patrick",
                "https://www.youtube.com/watch?v=PZWh4Kji7uA"
            ),
            isPinned = false,
            isEdited = false,
            status = SendingStatus.InProgress,
            date = 0,
            albumId = AlbumMediaId(id),
        )
    }
}

@Stable
data class MessageId(val value: Long)

@Stable
data class AlbumMediaId(val value: Long)