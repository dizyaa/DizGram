package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chatFilterFlow: Flow<List<ChatFilter>>
    val chatFlow: Flow<Chat>

    suspend fun loadChatsByFilter(filter: ChatFilter)
    suspend fun getChatById(id: ChatId): Chat
}