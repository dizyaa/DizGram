package dev.dizyaa.dizgram.feature.chat.domain

data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val chatPhoto: ChatPhoto?,
) {
    companion object {
        fun fake(id: Long) = Chat(
            id = ChatId(id),
            lastMessage = Message.mock(id),
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