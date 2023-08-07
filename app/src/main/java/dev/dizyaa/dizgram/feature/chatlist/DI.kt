package dev.dizyaa.dizgram.feature.chatlist

import dev.dizyaa.dizgram.feature.chatlist.data.ChatRepository
import dev.dizyaa.dizgram.feature.chatlist.data.TelegramChatRepository
import dev.dizyaa.dizgram.feature.chatlist.ui.ChatListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatListModule = module {
    single<ChatRepository> { TelegramChatRepository(get()) }
    viewModel { ChatListViewModel(get(), get()) }
}