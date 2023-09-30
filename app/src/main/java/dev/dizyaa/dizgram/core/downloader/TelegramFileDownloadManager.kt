package dev.dizyaa.dizgram.core.downloader

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chat.domain.needBeDownloaded
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramFileDownloadManager(
    context: TdContext,
) : TdRepository(context), FileDownloadManager {

    override val downloadedFlow: Flow<File> = getUpdatesFlow<TdApi.UpdateFile>()
        .map { file ->
            file.file.toDomain().also { photo ->
                if (photo.localFile.needBeDownloaded) {
                    download(photo.id)
                }
            }
        }
        .filterNot { it.localFile.needBeDownloaded }

    override suspend fun download(id: FileId) {
        execute<TdApi.File>(
            TdApi.DownloadFile(
                id.value,
                16,
                0,
                0,
                false,
            )
        )
    }
}