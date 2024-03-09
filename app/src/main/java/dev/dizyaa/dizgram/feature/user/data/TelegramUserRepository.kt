package dev.dizyaa.dizgram.feature.user.data

import dev.dizyaa.dizgram.core.telegram.TelegramContext
import dev.dizyaa.dizgram.feature.user.domain.User
import dev.dizyaa.dizgram.feature.user.domain.UserId
import dev.dizyaa.dizgram.feature.user.domain.toDomain
import org.drinkless.td.libcore.telegram.TdApi

class TelegramUserRepository(
    private val context: TelegramContext,
): UserRepository {

    override suspend fun getUserById(userId: UserId): User {
        return context.execute<TdApi.User>(TdApi.GetUser(userId.value)).toDomain()
    }

    override suspend fun getCurrentUser(): User {
        return context.execute<TdApi.User>(TdApi.GetMe()).toDomain()
    }

}