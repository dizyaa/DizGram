package dev.dizyaa.dizgram.feature.chat.domain

import dev.dizyaa.dizgram.feature.user.domain.UserId

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
                content = "Foxi 🦊",
                sender = MessageSender(UserId(id))
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

data class ChatId(val value: Long): SenderId

enum class ChatType {
    Direct, Group, Channel, Bot
}