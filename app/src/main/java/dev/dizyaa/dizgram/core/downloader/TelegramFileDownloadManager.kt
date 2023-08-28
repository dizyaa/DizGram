package dev.dizyaa.dizgram.core.downloader

import dev.dizyaa.dizgram.core.telegram.TdContext
import dev.dizyaa.dizgram.core.telegram.TdRepository
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomainPhoto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramFileDownloadManager(
    context: TdContext,
) : TdRepository(context), FileDownloadManager {

    override val downloadedFlow: Flow<File> = getUpdatesFlow<TdApi.UpdateFile>()
        .map { file ->
            file.file.toDomainPhoto().also { photo ->
                if (photo.needToDownload) {
                    download(photo.id)
                }
            }
        }
        .filterNot { it.needToDownload }

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