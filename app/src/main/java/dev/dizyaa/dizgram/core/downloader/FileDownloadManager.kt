package dev.dizyaa.dizgram.core.downloader

import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import kotlinx.coroutines.flow.Flow

interface FileDownloadManager {
    val downloadedFlow: Flow<File>

    suspend fun download(id: FileId)
}