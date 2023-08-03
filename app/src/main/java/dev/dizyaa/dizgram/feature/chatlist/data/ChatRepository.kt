package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import dev.dizyaa.dizgram.feature.chatlist.domain.FileId
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chatFilterFlow: Flow<List<ChatFilter>>
    val chatsFlow: Flow<Chat>
    val chatUpdatesFlow: Flow<ChatUpdate>

    suspend fun loadPhotoByFileId(chatId: ChatId, fileId: FileId)
    suspend fun loadChatsByFilter(filter: ChatFilter)
    suspend fun getChatById(id: ChatId): Chat
}