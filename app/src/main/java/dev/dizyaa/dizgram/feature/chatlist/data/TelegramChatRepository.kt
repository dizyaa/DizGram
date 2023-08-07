package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomainPhoto
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flattenMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatListFilter

class TelegramChatRepository(
    context: TdContext,
): TdRepository(context), ChatRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatFilterFlow: Flow<List<ChatFilter>> =
        getUpdatesFlow<TdApi.UpdateChatFilters>()
            .flatMapConcat { update ->
                flow {
                    update.chatFilters.map { it.toDomain() }
                }
            }

    private val fileIdToChatIdMap = mutableMapOf<FileId, ChatId>()

    override val chatsFlow: Flow<Chat> =
        getUpdatesFlow<TdApi.UpdateNewChat>().map {
            it.chat.toDomain().also { chat ->
                chat.chatPhoto?.let { photo ->
                    photo.big?.id?.let { id -> fileIdToChatIdMap.put(id, chat.id) }
                    photo.small?.id?.let { id -> fileIdToChatIdMap.put(id, chat.id) }
                }
            }
        }

    private val updatesChatPhoto: Flow<ChatUpdate> = getUpdatesFlow<TdApi.UpdateChatPhoto>()
        .map {
            ChatUpdate.ChatPhoto(
                chatId = ChatId(it.chatId),
                chatPhoto = it.photo?.toDomain(),
            )
        }

    private val updatesLastMessage: Flow<ChatUpdate> = getUpdatesFlow<TdApi.UpdateChatLastMessage>(TdApi.UpdateChatLastMessage.CONSTRUCTOR)
        .map {
            ChatUpdate.LastMessage(
                chatId = ChatId(it.chatId),
                message = it.lastMessage?.toDomain()
            )
        }

    private val updatePhotoDownload: Flow<ChatUpdate> = getUpdatesFlow<TdApi.UpdateFile>(TdApi.UpdateFile.CONSTRUCTOR)
        .filter { !it.file.toDomainPhoto().needToDownload }
        .map { update ->
            val photo = update.file.toDomainPhoto()
            val chatId = fileIdToChatIdMap[photo.id] ?: ChatId(-1L) // skip

            ChatUpdate.Photo(
                chatId = chatId,
                photo = photo,
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatUpdatesFlow: Flow<ChatUpdate> = flowOf(
        updatePhotoDownload,
        updatesLastMessage,
        updatesChatPhoto,
    ).flattenMerge()

    override suspend fun loadChatsByFilter(filter: ChatFilter) {
        execute<TdApi.Ok>(
            TdApi.LoadChats(
                if (filter == ChatFilter.Main) {
                    null
                } else {
                    ChatListFilter(filter.id)
                },
                LIMIT
            ),
            onError = {
                if (it.code == 404) {
                    // TODO: Returns a 404 error if all chats have been loaded.
                }
            }
        )
    }

    override suspend fun loadPhotoByFileId(chatId: ChatId, fileId: FileId) {
        execute<TdApi.File>(
            TdApi.DownloadFile(
                fileId.value,
                16,
                0,
                0,
                false,
            )
        )
    }

    override suspend fun getChatById(id: ChatId): Chat {
        TODO("Not yet implemented")
    }

    companion object {
        private const val LIMIT = 50
    }
}