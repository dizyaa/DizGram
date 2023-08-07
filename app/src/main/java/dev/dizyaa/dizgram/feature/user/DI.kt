package dev.dizyaa.dizgram.feature.user

import dev.dizyaa.dizgram.feature.user.data.TelegramUserRepository
import dev.dizyaa.dizgram.feature.user.data.UserRepository
import org.koin.dsl.module

val userModule = module {
    single<UserRepository> { TelegramUserRepository(get()) }
}