package dev.dizyaa.dizgram.feature.chatlist.domain

data class Chat(
    val id: ChatId,
    val lastMessage: Message?,
    val name: String,
    val photo: ChatPhoto?,
)

class ChatId(val value: Long)

enum class ChatType {
    Direct, Group, Channel, Bot
}