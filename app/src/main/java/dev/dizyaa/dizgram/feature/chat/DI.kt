package dev.dizyaa.dizgram.feature.chat

import dev.dizyaa.dizgram.feature.chat.data.TelegramChatRepository
import dev.dizyaa.dizgram.feature.chat.ui.ChatViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val chatModule = module {
    viewModel { params ->
        val repository = TelegramChatRepository(params.get(), get())
        ChatViewModel(repository, get(), get())
    }
}