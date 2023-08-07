package dev.dizyaa.dizgram.feature.user.data

import dev.dizyaa.dizgram.feature.user.domain.User
import dev.dizyaa.dizgram.feature.user.domain.UserId

interface UserRepository {
    suspend fun getUserById(userId: UserId): User
    suspend fun getCurrentUser(): User
}