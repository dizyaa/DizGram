package dev.dizyaa.dizgram.core

import dev.dizyaa.dizgram.core.telegram.TdContext
import org.koin.dsl.module

val coreModule = module {
    single(createdAtStart = true) { TdContext() }
}