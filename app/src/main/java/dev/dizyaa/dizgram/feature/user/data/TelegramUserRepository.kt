package dev.dizyaa.dizgram.feature.user.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.user.domain.User
import dev.dizyaa.dizgram.feature.user.domain.UserId
import dev.dizyaa.dizgram.feature.user.domain.toDomain
import org.drinkless.td.libcore.telegram.TdApi

class TelegramUserRepository(
    context: TdContext
): TdRepository(context), UserRepository {

    override suspend fun getUserById(userId: UserId): User {
        return execute<TdApi.User>(TdApi.GetUser(userId.value)).toDomain()
    }

    override suspend fun getCurrentUser(): User {
        return execute<TdApi.User>(TdApi.GetMe()).toDomain()
    }

}