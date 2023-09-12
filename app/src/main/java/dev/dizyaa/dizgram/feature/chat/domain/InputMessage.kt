package dev.dizyaa.dizgram.feature.chat.domain

data class InputMessage(
    val content: MessageContent?,
    val replyMessageId: MessageId,
    val date: Int,
)