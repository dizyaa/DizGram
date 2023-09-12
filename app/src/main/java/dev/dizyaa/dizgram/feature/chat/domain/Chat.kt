package dev.dizyaa.dizgram.feature.chat.domain

data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val chatPhoto: ChatPhoto?,
    val canSendMessage: Boolean,
    val draftMessage: InputMessage?,
) {
    companion object {
        fun fake(id: Long) = Chat(
            id = ChatId(id),
            lastMessage = Message.mock(id),
            name = "Chat #$id",
            chatPhoto = ChatPhoto(
                File.fake(),
                File.fake(),
                File.fake(),
            ),
            canSendMessage = true,
            draftMessage = null,
        )
    }
}

data class ChatId(val value: Long): SenderId