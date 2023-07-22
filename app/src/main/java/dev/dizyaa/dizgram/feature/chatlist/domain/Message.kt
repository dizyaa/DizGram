package dev.dizyaa.dizgram.feature.chatlist.domain

data class Message(
    val id: MessageId,
    val chatId: ChatId,

)

class MessageId(val value: Long)
