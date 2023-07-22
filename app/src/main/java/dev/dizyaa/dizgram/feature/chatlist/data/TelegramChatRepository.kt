package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatListFilter
import timber.log.Timber

class TelegramChatRepository(
    context: TdContext,
): TdRepository(context), ChatRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatFilterFlow: Flow<List<ChatFilter>> =
        getUpdatesFlow<TdApi.UpdateChatFilters>()
            .flatMapConcat { update ->
                Timber.d(update.toString())
                flow {
                    update.chatFilters.map { it.toDomain() }
                }
            }

    // TODO: Add separate updates for changes chat fields, because will be reported through
    override val chatFlow: Flow<Chat> =
        getUpdatesFlow<TdApi.UpdateNewChat>().map {
            Timber.d(it.toString())
            it.chat.toDomain()
        }

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

    override suspend fun getChatById(id: ChatId): Chat {
        TODO("Not yet implemented")
    }

    companion object {
        private const val LIMIT = 50
    }
}