package dev.dizyaa.dizgram.feature.chatlist.domain

import androidx.compose.runtime.Stable

@Stable
data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val chatPhoto: ChatPhoto?,
) {
    companion object {
        fun fake(id: Long) = Chat(
            id = ChatId(id),
            lastMessage = Message(
                id = MessageId(id),
                chatId = ChatId(id),
                content = "Foxi 🦊"
            ),
            name = "Chat #$id",
            chatPhoto = ChatPhoto(
                Photo.fake(),
                Photo.fake(),
                Photo.fake(),
            )
        )
    }
}

class ChatId(val value: Long)

enum class ChatType {
    Direct, Group, Channel, Bot
}