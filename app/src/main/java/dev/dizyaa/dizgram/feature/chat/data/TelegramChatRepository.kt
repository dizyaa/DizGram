package dev.dizyaa.dizgram.feature.chat.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapNotNull
import org.drinkless.td.libcore.telegram.TdApi

class TelegramChatRepository(
    override val chatId: ChatId,
    context: TdContext,
): TdRepository(context), ChatRepository {

    override suspend fun getChat(): Chat {
        return execute<TdApi.Chat>(TdApi.GetChat(chatId.value)).toDomain()
    }

    override val messages: Flow<Message> = flow {
        execute<TdApi.Ok>(TdApi.OpenChat(chatId.value))

        execute<TdApi.Messages>(
            TdApi.GetChatHistory(
                chatId.value,
                0,
                0,
                20,
                false,
            )
        ).messages
            .mapNotNull { it.toDomain() }
            .forEach { emit(it) }

        emitAll(
            flow = getUpdatesFlow<TdApi.UpdateNewMessage>()
                .mapNotNull { it.message.toDomain() }
                .filter { it.chatId == chatId }
        )
    }

    override val messageUpdates: Flow<MessageUpdate> =
        getUpdatesFlow<TdApi.Update>().mapMessageUpdateToDomain()

    override suspend fun closeChat() {
        execute<TdApi.Ok>(TdApi.CloseChat(chatId.value))
    }
}