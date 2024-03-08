package dev.dizyaa.dizgram.feature.user

import dev.dizyaa.dizgram.feature.user.data.TelegramUserRepository
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> {
        TelegramUserRepository(get(), CoroutineScope(Dispatchers.IO))
    }
}