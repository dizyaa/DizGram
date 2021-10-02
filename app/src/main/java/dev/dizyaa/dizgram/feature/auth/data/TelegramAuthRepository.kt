package dev.dizyaa.dizgram.feature.auth.data

import dev.dizyaa.dizgram.core.telegram.TdError
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import dev.dizyaa.dizgram.feature.configuration.Configuration
import dev.dizyaa.dizgram.feature.datagates.DataGatesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationState
import timber.log.Timber

class TelegramAuthRepository(
    private val dataGatesManager: DataGatesManager,
    private val configuration: Configuration,
): AuthRepository {

    private val authFlow: MutableStateFlow<AuthStatus?> = MutableStateFlow(null)

    private var client = Client.create(
        ::handleResult,
        ::handleException,
        ::handleExceptionDefault,
    )

    override fun getAuthStatusFlow(): Flow<AuthStatus> {
        return authFlow.map { it ?: AuthStatus.WaitPhoneNumber }
    }

    override suspend fun authByPhoneNumber(phoneNumber: String) {
        sendFunction(
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
        sendFunction(TdApi.CheckAuthenticationCode(code))
    }

    override suspend fun authByPassword(password: String) {
        sendFunction(TdApi.CheckAuthenticationPassword(password))
    }

    private fun handleResult(obj: TdApi.Object) {
        if (obj.constructor == TdApi.UpdateAuthorizationState.CONSTRUCTOR) {
            handleAuthorizationState((obj as TdApi.UpdateAuthorizationState).authorizationState)
        }
    }

    private fun handleException(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun handleExceptionDefault(throwable: Throwable) {
        handleException(throwable)
    }

    private fun handleAuthorizationState(state: AuthorizationState) {
        Timber.i("AuthState -> $state")

        when (state.constructor) {
            TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                setupTdLib()
            }
            TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                checkEncryptionKey()
            }
            else -> Unit
        }

        authFlow.value = mapTdAuthStateToAuthStatus(state)
    }

    private fun setupTdLib() {
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

        sendFunction(params)
    }

    private fun checkEncryptionKey() {
        sendFunction(TdApi.CheckDatabaseEncryptionKey())
    }

    private fun sendFunction(function: TdApi.Function) {
        client.send(function) {
            if (it is TdApi.Error) {
                handleException(TdError(it))
            }
        }
    }

    private fun mapTdAuthStateToAuthStatus(state: AuthorizationState): AuthStatus {
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


    companion object {
        private const val API_ID = 2592391
        private const val API_HASH = "c934347181b54eef14c74030ee9f5278"
    }
}