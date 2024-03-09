package dev.dizyaa.dizgram.feature.auth.data

import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val authStatus: Flow<AuthStatus>

    suspend fun getAuthStatus(): AuthStatus
    suspend fun authByPhoneNumber(phoneNumber: String)
    suspend fun authByCode(code: String)
    suspend fun authByPassword(password: String)

    suspend fun loadParams()
    suspend fun loadEncryptedKey()
}
