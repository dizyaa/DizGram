package dev.dizyaa.dizgram.feature.chatlist.ui.model

import androidx.compose.runtime.Stable
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.SizedPhoto

@Stable
data class ChatCard(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val sizedPhoto: SizedPhoto?,
    val lastMessageFromMyself: Boolean,
) {
    companion object {
        fun fake(id: Long) = ChatCard(
            id = ChatId(id),
            lastMessage = Message.mock(id),
            name = "Chat #$id",
            sizedPhoto = null,
            lastMessageFromMyself = false,
        )
    }
}