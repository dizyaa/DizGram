package dev.dizyaa.dizgram.feature.chatlist

import dev.dizyaa.dizgram.feature.chatlist.data.ChatListRepository
import dev.dizyaa.dizgram.feature.chatlist.data.TelegramChatListRepository
import dev.dizyaa.dizgram.feature.chatlist.ui.ChatListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatListModule = module {
    single<ChatListRepository> {
        TelegramChatListRepository(get(), CoroutineScope(Dispatchers.IO))
    }
    viewModel { ChatListViewModel(get(), get(), get()) }
}