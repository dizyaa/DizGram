package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import kotlinx.coroutines.flow.Flow

interface ChatListRepository {
    val chatFilterFlow: Flow<List<ChatFilter>>
    val chatsFlow: Flow<Chat>
    val chatUpdatesFlow: Flow<ChatUpdate>
    suspend fun loadChatsByFilter(filter: ChatFilter)
}