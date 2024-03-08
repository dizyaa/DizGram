package dev.dizyaa.dizgram.feature.auth

import dev.dizyaa.dizgram.feature.auth.data.AuthRepository
import dev.dizyaa.dizgram.feature.auth.data.TelegramAuthRepository
import dev.dizyaa.dizgram.feature.auth.ui.AuthViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val authModule = module {
    single<AuthRepository> {
        TelegramAuthRepository(get(), get(), get(), CoroutineScope(Dispatchers.IO))
    }
    viewModel { AuthViewModel(get()) }
}