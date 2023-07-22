package dev.dizyaa.dizgram.core

import dev.dizyaa.dizgram.core.telegram.CoreClient
import dev.dizyaa.dizgram.core.telegram.TdContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

val coreModule = module {
    single { TdContext(CoroutineScope(Dispatchers.IO + SupervisorJob())) }
    single(createdAtStart = true) {
        CoreClient(
            get(),
            get(),
            get(),
        )
    }
}