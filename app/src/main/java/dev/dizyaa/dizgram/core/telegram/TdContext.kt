package dev.dizyaa.dizgram.core.telegram

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber

class TdContext(
    private val coroutineScope: CoroutineScope,
) {
    internal val client: Client = Client.create(
        ::handleResult,
        ::handleException,
        ::handleExceptionDefault,
    )

    private val _updates: MutableSharedFlow<TdApi.Object> =
        MutableSharedFlow(
            replay = 40,
            extraBufferCapacity = 100,
            onBufferOverflow = BufferOverflow.SUSPEND
        )
    val updates: SharedFlow<TdApi.Object> = _updates

    private fun handleResult(obj: TdApi.Object) {
        coroutineScope.launch {
            _updates.emit(obj)
        }
    }

    private fun handleException(throwable: Throwable) {
        Timber.e(throwable)
    }

    private fun handleExceptionDefault(throwable: Throwable) {
        handleException(throwable)
    }
}