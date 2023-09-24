package dev.dizyaa.dizgram.feature.chat.domain

data class InputMessage(
    val content: InputMessageContent?,
    val replyMessageId: MessageId?,
)