package dev.dizyaa.dizgram.feature.auth.data

import dev.dizyaa.dizgram.core.telegram.TelegramContext
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import dev.dizyaa.dizgram.feature.configuration.Configuration
import dev.dizyaa.dizgram.feature.datagates.DataGatesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramAuthRepository(
    private val dataGatesManager: DataGatesManager,
    private val configuration: Configuration,
    private val context: TelegramContext,
): AuthRepository {

    private val authorizationState = context.getUpdatesFlow<TdApi.UpdateAuthorizationState>()

    override val authStatus: Flow<AuthStatus> = authorizationState
        .map { mapAuthState(it.authorizationState) }

    override suspend fun authByPhoneNumber(phoneNumber: String) {
        setPhoneNumber(phoneNumber)
    }

    override suspend fun authByCode(code: String) {
        context.execute<TdApi.Object>(TdApi.CheckAuthenticationCode(code))
    }

    override suspend fun authByPassword(password: String) {
        context.execute<TdApi.Object>(TdApi.CheckAuthenticationPassword(password))
    }

    override suspend fun loadParams() {
        setupTdLib()
    }

    override suspend fun loadEncryptedKey() {
        checkEncryptionKey()
    }

    private suspend fun setPhoneNumber(phoneNumber: String) {
        context.execute<TdApi.Ok>(
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

    private suspend fun setupTdLib() {
        val params = TdApi.SetTdlibParameters(
            TdApi.TdlibParameters(
                false,
                dataGatesManager.databaseDir,
                dataGatesManager.filesDir,
                true,
                true,
                true,
                false,
                API_ID,
                API_HASH,
                configuration.systemLanguageCode,
                configuration.deviceModel,
                configuration.systemVersion,
                configuration.applicationVersion,
                true,
                false,
            )
        )

        context.execute<TdApi.Ok>(params)
    }

    private suspend fun checkEncryptionKey() {
        context.execute<TdApi.Object>(TdApi.CheckDatabaseEncryptionKey())
    }

    companion object {
        private const val API_ID = 2592391
        private const val API_HASH = "c934347181b54eef14c74030ee9f5278"
    }

    private fun mapAuthState(state: TdApi.AuthorizationState): AuthStatus {
        return when (state.constructor) {
            TdApi.AuthorizationStateLoggingOut.CONSTRUCTOR ->
                AuthStatus.LoggingOut
            TdApi.AuthorizationStateClosing.CONSTRUCTOR ->
                AuthStatus.Closing
            TdApi.AuthorizationStateClosed.CONSTRUCTOR ->
                AuthStatus.Closed
            TdApi.AuthorizationStateReady.CONSTRUCTOR ->
                AuthStatus.Ready
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR ->
                AuthStatus.WaitParams
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR ->
                AuthStatus.WaitEncryptedKey
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
            else -> AuthStatus.WaitParams
        }
    }
}