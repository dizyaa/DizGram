package dev.dizyaa.dizgram.core.downloader

import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import kotlinx.coroutines.flow.Flow

interface FileDownloadManager {
    fun getDownloadedFileFlow(
        type: File.Type,
        allowUnknownType: Boolean = false
    ): Flow<File>

    /**
     * Just trigger downloader manager to download. Final file will emit in [getDownloadedFileFlow]
     */
    suspend fun downloadById(id: FileId, type: File.Type)
    suspend fun cancelDownloadById(id: FileId)
}