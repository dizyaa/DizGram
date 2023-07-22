package dev.dizyaa.dizgram.core.telegram

import dev.dizyaa.dizgram.feature.configuration.Configuration
import dev.dizyaa.dizgram.feature.datagates.DataGatesManager
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

class CoreClient(
    private val dataGatesManager: DataGatesManager,
    private val configuration: Configuration,
    context: TdContext
): TdRepository(context) {

    suspend fun start() {
        getUpdatesFlow<TdApi.UpdateAuthorizationState>()
            .collect {
                processAuth(it.authorizationState)
            }
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

        execute<TdApi.Object>(params)
    }

    private suspend fun processAuth(state: TdApi.AuthorizationState) {
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
    }

    private suspend fun checkEncryptionKey() {
        execute<TdApi.Object>(TdApi.CheckDatabaseEncryptionKey())
    }

    companion object {
        private const val API_ID = 2592391
        private const val API_HASH = "c934347181b54eef14c74030ee9f5278"
    }
}