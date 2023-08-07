package dev.dizyaa.dizgram.feature.chatlist.ui.model

import androidx.compose.runtime.Stable
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.Photo
import dev.dizyaa.dizgram.feature.user.domain.UserId

@Stable
data class ChatCard(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val chatPhoto: ChatPhoto?,
    val lastMessageFromMyself: Boolean,
) {
    companion object {
        fun fake(id: Long) = ChatCard(
            id = ChatId(id),
            lastMessage = Message(
                id = MessageId(id),
                chatId = ChatId(id),
                content = "Foxi 🦊",
                sender = UserId(id)
            ),
            name = "Chat #$id",
            chatPhoto = ChatPhoto(
                Photo.fake(),
                Photo.fake(),
                Photo.fake(),
            ),
            lastMessageFromMyself = false,
        )
    }
}