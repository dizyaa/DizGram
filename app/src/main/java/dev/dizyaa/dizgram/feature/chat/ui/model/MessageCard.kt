package dev.dizyaa.dizgram.feature.chat.ui.model

import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.SenderId
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.user.domain.UserId

data class MessageCard(
    val id: MessageId,
    val senderId: SenderId,
    val sendingStatus: SendingStatus,
    val authorName: String?,
    val fromMe: Boolean,
    val date: Int,
    val type: MessageCardType,
) {
    companion object {
        fun mock(id: Long = -1L, fromMe: Boolean = true) = MessageCard(
            id = MessageId(id),
            senderId = UserId(id),
            sendingStatus = SendingStatus.InProgress,
            authorName = null,
            fromMe = fromMe,
            date = 0,
            type = MessageCardType.WithMedia(text = "Ttt", files = emptyList())
        )
    }
}

sealed class MessageCardType {
    data class WithMedia(
        val text: String,
        val files: List<File>,
    ) : MessageCardType()

    object Unsupported : MessageCardType()
}