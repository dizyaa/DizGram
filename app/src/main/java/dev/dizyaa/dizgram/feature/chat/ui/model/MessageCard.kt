package dev.dizyaa.dizgram.feature.chat.ui.model

import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.SenderId
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.user.domain.UserId

data class MessageCard(
    val id: MessageId,
    val senderId: SenderId,
    val contentText: String,
    val contentLink: String?,
    val contentImages: List<ChatPhoto>,
    val sendingStatus: SendingStatus,
    val authorName: String?,
    val fromMe: Boolean,
    val date: Int,
) {
    companion object {
        fun mock(id: Long = -1L, fromMe: Boolean = true) = MessageCard(
            id = MessageId(id),
            senderId = UserId(id),
            contentText = "Neque porro quisquam est qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit...",
            contentLink = "https://google.com/",
            contentImages = emptyList(),
            sendingStatus = SendingStatus.InProgress,
            authorName = null,
            fromMe = fromMe,
            date = 0,
        )
    }
}