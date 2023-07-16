package dev.dizyaa.dizgram.feature.chatlist.domain

data class Chat(
    val id: ChatId,
)

class ChatId(val value: Long)

enum class ChatType {
    Direct, Group, Channel, Bot
}