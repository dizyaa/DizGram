package dev.dizyaa.dizgram.core

import dev.dizyaa.dizgram.core.downloader.FileDownloadManager
import dev.dizyaa.dizgram.core.downloader.TelegramFileDownloadManager
import dev.dizyaa.dizgram.core.telegram.TdContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val coreModule = module {
    single(createdAtStart = true) { TdContext() }
    single <FileDownloadManager> {
        TelegramFileDownloadManager(get(), CoroutineScope(Dispatchers.IO))
    }
}