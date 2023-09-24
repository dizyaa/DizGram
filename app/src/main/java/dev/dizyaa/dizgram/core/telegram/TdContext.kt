package dev.dizyaa.dizgram.core.telegram

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

class TdContext(
) {
    internal val client: Client = Client.create(
        ::handleResult,
        ::handleException,
        ::handleExceptionDefault,
    )

    private val _updates: MutableSharedFlow<TdApi.Object> =
        MutableSharedFlow(
            replay = 500,
            extraBufferCapacity = 500,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val updates: SharedFlow<TdApi.Object> = _updates

    private fun handleResult(obj: TdApi.Object) {
        _updates.tryEmit(obj)
    }

    private fun handleException(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun handleExceptionDefault(throwable: Throwable) {
        handleException(throwable)
    }
}