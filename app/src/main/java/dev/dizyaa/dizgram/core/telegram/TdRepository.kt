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
    internal suspend inline fun <reified T: TdApi.Object> execute(function: TdApi.Function) =
        suspendCoroutine { continuation ->
            context.client.send(
                function,
                {
                    if (it is T) {
                        continuation.resume(it)
                    } else {
                        val ex = Exception("Response of ${function::class} not is ${T::class}!")
                        continuation.resumeWithException(ex)
                    }
                },
                {
                    continuation.resumeWithException(it)
                }
            )
        }

    internal inline fun <reified T: TdApi.Object> getUpdatesFlow(): Flow<T> =
        context.updates.filterIsInstance(T::class)
}