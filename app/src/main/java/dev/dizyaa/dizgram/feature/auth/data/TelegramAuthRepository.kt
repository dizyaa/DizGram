package dev.dizyaa.dizgram.feature.auth.data

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramAuthRepository(
    context: TdContext,
): TdRepository(context), AuthRepository {

    override val authStatus: Flow<AuthStatus> = getUpdatesFlow<TdApi.UpdateAuthorizationState>()
        .map { mapAuthState(it.authorizationState) }

    override suspend fun authByPhoneNumber(phoneNumber: String) {
        execute<TdApi.Object>(
            TdApi.SetAuthenticationPhoneNumber(
                phoneNumber,
                TdApi.PhoneNumberAuthenticationSettings(
                    true,
                    true,
                    false,
                    false,
                    arrayOf()
                )
            )
        )
    }

    override suspend fun authByCode(code: String) {
        execute<TdApi.Object>(TdApi.CheckAuthenticationCode(code))
    }

    override suspend fun authByPassword(password: String) {
        execute<TdApi.Object>(TdApi.CheckAuthenticationPassword(password))
    }

    private fun mapAuthState(state: TdApi.AuthorizationState): AuthStatus {
        return when (state.constructor) {
//            TdApi.AuthorizationStateLoggingOut ->
//            TdApi.AuthorizationStateClosing ->
//            TdApi.AuthorizationStateClosed ->
            TdApi.AuthorizationStateReady.CONSTRUCTOR ->
                AuthStatus.Ready
            TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR ->
                AuthStatus.WaitPhoneNumber
            TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR ->
                AuthStatus.WaitPassword
            TdApi.AuthorizationStateWaitRegistration.CONSTRUCTOR ->
                AuthStatus.WaitRegistration
            TdApi.AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR ->
                AuthStatus.WaitOtherDeviceConfirmation
            TdApi.AuthorizationStateWaitCode.CONSTRUCTOR ->
                AuthStatus.WaitCode
            else -> AuthStatus.WaitPhoneNumber
        }
    }
}