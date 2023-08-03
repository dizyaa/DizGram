package dev.dizyaa.dizgram.feature.chatlist.domain

sealed class ChatUpdate(open val chatId: ChatId) {
    data class ChatPhoto(
        override val chatId: ChatId,
        val chatPhoto: dev.dizyaa.dizgram.feature.chatlist.domain.ChatPhoto?
    ) : ChatUpdate(chatId)

    data class LastMessage(override val chatId: ChatId, val message: Message?) : ChatUpdate(chatId)

    data class Photo(
        override val chatId: ChatId,
        val photo: dev.dizyaa.dizgram.feature.chatlist.domain.Photo,
    ) : ChatUpdate(chatId)
}