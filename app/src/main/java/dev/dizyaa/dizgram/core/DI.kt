package dev.dizyaa.dizgram.core

import dev.dizyaa.dizgram.core.downloader.FileDownloadManager
import dev.dizyaa.dizgram.core.downloader.TelegramFileDownloadManager
import org.koin.dsl.module

val coreModule = module {
    single <FileDownloadManager> {
        TelegramFileDownloadManager(get())
    }
}