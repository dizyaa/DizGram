package dev.dizyaa.dizgram.feature.chatlist

import dev.dizyaa.dizgram.feature.chatlist.data.ChatListRepository
import dev.dizyaa.dizgram.feature.chatlist.data.TelegramChatListRepository
import dev.dizyaa.dizgram.feature.chatlist.ui.ChatListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatListModule = module {
    single<ChatListRepository> { TelegramChatListRepository(get()) }
    viewModel { ChatListViewModel(get(), get()) }
}