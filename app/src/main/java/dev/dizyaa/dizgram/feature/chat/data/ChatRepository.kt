package dev.dizyaa.dizgram.feature.chat.data

import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chatId: ChatId

    val messageUpdates: Flow<MessageUpdate>
    val messages: Flow<Message>

    suspend fun getChat(): Chat

    suspend fun closeChat()
}