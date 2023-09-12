package dev.dizyaa.dizgram.feature.chat.data

import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chatId: ChatId

    val messageUpdates: Flow<MessageUpdate>
    val newMessages: Flow<Message>

    suspend fun getChatMessages(fromMessage: MessageId, limit: Int, offset: Int): List<Message>

    suspend fun getChat(): Chat

    suspend fun sendTextMessage(message: String): Message

    suspend fun setDraftMessage(message: InputMessage?)

    suspend fun openChat()
    suspend fun closeChat()
}