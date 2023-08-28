package dev.dizyaa.dizgram.feature.chatlist.ui.model

import androidx.compose.runtime.Stable
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.Message

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
            lastMessage = Message.mock(id),
            name = "Chat #$id",
            chatPhoto = ChatPhoto(
                File.fake(),
                File.fake(),
                File.fake(),
            ),
            lastMessageFromMyself = false,
        )
    }
}