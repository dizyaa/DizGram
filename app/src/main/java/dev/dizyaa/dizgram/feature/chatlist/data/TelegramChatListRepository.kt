package dev.dizyaa.dizgram.feature.chatlist.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatUpdate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatListFilter

class TelegramChatListRepository(
    context: TdContext,
): TdRepository(context), ChatListRepository {

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatFilterFlow: Flow<List<ChatFilter>> =
        getUpdatesFlow<TdApi.UpdateChatFilters>()
            .flatMapConcat { update ->
                flow {
                    update.chatFilters.map { it.toDomain() }
                }
            }

    override val chatsFlow: Flow<Chat> =
        getUpdatesFlow<TdApi.UpdateNewChat>().map {
            it.chat.toDomain()
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val chatUpdatesFlow: Flow<ChatUpdate> =
        getUpdatesFlow<TdApi.Update>().mapChatUpdateToDomain()

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

    companion object {
        private const val LIMIT = 50
    }
}