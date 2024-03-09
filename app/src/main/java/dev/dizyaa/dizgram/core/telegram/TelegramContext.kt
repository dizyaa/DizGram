package dev.dizyaa.dizgram.core.telegram

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TelegramContext {

    val client: Client = Client.create(
        ::handleResult,
        ::handleUpdateException,
        ::handleDefaultException
    )

    /**
     * Execute Telegram function and wait response
     */
    suspend inline fun <reified T: TdApi.Object> execute(
        function: TdApi.Function,
        noinline onError: ((TdApi.Error) -> Unit)? = null,
    ) = suspendCoroutine { continuation ->
        client.send(
            function,
            {
                when (it) {
                    is TdApi.Error -> if (onError != null) {
                        onError(it)
                    } else {
                        continuation.resumeWithException(
                            exception = Exception("TdLib ERR: ${it.code} - ${it.message}")
                        )
                    }

                    is T -> continuation.resume(it)

                    else -> continuation.resumeWithException(
                        exception = Exception("Response of ${function::class} not is ${T::class}!")
                    )
                }
            },
            {
                continuation.resumeWithException(it)
            }
        )
    }

    private val updates: MutableSharedFlow<TdApi.Object> =
        MutableSharedFlow(
            replay = 50,
            extraBufferCapacity = 50,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )


    private fun handleResult(obj: TdApi.Object) {
        updates.tryEmit(obj)
    }

    private fun handleUpdateException(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun handleDefaultException(throwable: Throwable) {
        handleUpdateException(throwable)
    }

    suspend fun close() {
        execute<TdApi.Ok>(TdApi.Close())
    }

    internal inline fun <reified T: TdApi.Update> getUpdatesFlow(): Flow<T> =
        updates.filterIsInstance<T>()
}