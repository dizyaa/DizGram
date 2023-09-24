package dev.dizyaa.dizgram.feature.chat.data

import dev.dizyaa.dizgram.core.utils.filteredMap
import dev.dizyaa.dizgram.feature.chat.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.MessageUpdateType
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.drinkless.td.libcore.telegram.TdApi

fun Flow<TdApi.Update>.mapMessageUpdateToDomain(): Flow<MessageUpdate> {
    return this
//        .filteredMap<TdApi.UpdateMessageSendAcknowledged> {
//            MessageUpdate()
//        }
        .filteredMap<TdApi.UpdateMessageSendSucceeded> {
            it.message.toDomain().chatId to MessageUpdateType.SendStatus(
                status = SendingStatus.Unread
            )
        }
        .filteredMap<TdApi.UpdateMessageSendFailed> {
            it.message.toDomain().chatId to MessageUpdateType.SendStatus(
                status = SendingStatus.Error
            )
        }
        .filteredMap<TdApi.UpdateMessageContent> {
            ChatId(it.messageId) to MessageUpdateType.Content(
                content = it.newContent.toDomain()
            )
        }
        .filteredMap<TdApi.UpdateMessageEdited> {
            ChatId(it.messageId) to MessageUpdateType.IsEdited(isEdited = true)
        }
//        .filteredMap<TdApi.UpdateMessageIsPinned> {
//            MessageUpdate()
//        }
//        .filteredMap<TdApi.UpdateMessageInteractionInfo> {
//            MessageUpdate()
//        }
//        .filteredMap<TdApi.UpdateMessageContentOpened> {
//            MessageUpdate()
//        }
//        .filteredMap<TdApi.UpdateMessageMentionRead> {
//            MessageUpdate()
//        }
//        .filteredMap<TdApi.UpdateMessageLiveLocationViewed> {
//            MessageUpdate()
//        }
        .filterIsInstance()
}

typealias MessageUpdate = Pair<MessageId, MessageUpdateType>