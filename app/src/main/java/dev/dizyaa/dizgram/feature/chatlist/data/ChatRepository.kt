package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import kotlinx.coroutines.flow.StateFlow

interface ChatRepository {
    val chatList: StateFlow<List<Chat>>
    suspend fun getChatById(id: ChatId): Chat
}