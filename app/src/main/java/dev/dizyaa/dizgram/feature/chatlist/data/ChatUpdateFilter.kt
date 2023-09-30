package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.core.utils.filteredMap
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.drinkless.td.libcore.telegram.TdApi

fun Flow<TdApi.Update>.mapChatUpdateToDomain(): Flow<ChatUpdate> {
    return this
        .filteredMap<TdApi.UpdateChatPhoto> {
            ChatUpdate.ChatPhoto(
                chatId = ChatId(it.chatId),
                sizedPhoto = it.photo?.toDomain(),
            )
        }
        .filteredMap<TdApi.UpdateChatLastMessage> {
            ChatUpdate.LastMessage(
                chatId = ChatId(it.chatId),
                message = it.lastMessage?.toDomain()
            )
        }
        .filterIsInstance()
}