package dev.dizyaa.dizgram.feature.chatlist.domain

import androidx.compose.runtime.Stable

@Stable
data class Message(
    val id: MessageId,
    val chatId: ChatId,
    val content: String,
)

@Stable
class MessageId(val value: Long)
