package dev.dizyaa.dizgram.core.telegram

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

abstract class TdRepository(
    private val context: TdContext,
) {

    /**
     * Execute Telegram function and wait response
     */
    internal suspend inline fun <reified T: TdApi.Object> execute(
        function: TdApi.Function,
        crossinline onError: (TdApi.Error) -> Unit = { }
    ) = suspendCoroutine { continuation ->
        context.client.send(
            function,
            {
                when (it) {
                    is T -> continuation.resume(it)

                    is TdApi.Error -> onError(it)

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

    internal inline fun <reified T: TdApi.Update> getUpdatesFlow(): Flow<T> =
        context.updates.filterIsInstance(T::class)
}