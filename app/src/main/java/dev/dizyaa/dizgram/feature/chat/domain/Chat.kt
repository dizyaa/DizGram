package dev.dizyaa.dizgram.feature.chat.domain

data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val sizedPhoto: SizedPhoto?,
    val canSendMessage: Boolean,
    val draftMessage: InputMessage?,
) {
    companion object {
        @Suppress("UNUSED")
        fun fake(id: Long) = Chat(
            id = ChatId(id),
            lastMessage = Message.mock(id),
            name = "Chat #$id",
            sizedPhoto = null,
            canSendMessage = true,
            draftMessage = null,
        )
    }
}

data class ChatId(val value: Long): SenderId