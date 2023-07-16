package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import kotlinx.coroutines.flow.StateFlow

class TelegramChatRepository: ChatRepository {

    override val chatList: StateFlow<List<Chat>>
        get() = TODO("Not yet implemented")

    override suspend fun getChatById(id: ChatId): Chat {
        TODO("Not yet implemented")
    }

}