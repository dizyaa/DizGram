package dev.dizyaa.dizgram.feature.chat.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.data.mappers.toTdApi
import dev.dizyaa.dizgram.feature.chat.data.mappers.toTdDraftMessage
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramChatRepository(
    override val chatId: ChatId,
    context: TdContext,
    coroutineScope: CoroutineScope,
): TdRepository(context, coroutineScope), ChatRepository {

    override suspend fun getChat(): Chat {
        return execute<TdApi.Chat>(TdApi.GetChat(chatId.value)).toDomain()
    }

    override val newMessages: Flow<Message> = getUpdatesFlow<TdApi.UpdateNewMessage>()
        .map { it.message.toDomain() }
        .filter { it.chatId == chatId }

    override val messageUpdates: Flow<MessageUpdate> =
        getUpdatesFlow<TdApi.Update>().mapMessageUpdateToDomain()

    override suspend fun getChatMessages(
        fromMessage: MessageId,
        limit: Int,
        offset: Int
    ): List<Message> {
        return execute<TdApi.Messages>(
            TdApi.GetChatHistory(
                chatId.value,
                fromMessage.value,
                offset,
                limit,
                false,
            )
        ).messages
            .map { it.toDomain() }
    }

    override suspend fun sendMessage(message: InputMessage): Message {
        return execute<TdApi.Message>(
            TdApi.SendMessage(
                chatId.value,
                0L,
                message.replyMessageId?.value ?: 0,
                null,
                null,
                message.content?.toTdApi(),
            )
        ).toDomain()
    }

    override suspend fun setDraftMessage(message: InputMessage?) {
        execute<TdApi.Ok>(
            TdApi.SetChatDraftMessage(
                chatId.value,
                0L,
                message?.toTdDraftMessage(),
            )
        )
    }

    override suspend fun openChat() {
        execute<TdApi.Ok>(TdApi.OpenChat(chatId.value))
    }

    override suspend fun closeChat() {
        execute<TdApi.Ok>(TdApi.CloseChat(chatId.value))
    }
}