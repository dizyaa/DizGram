package dev.dizyaa.dizgram.core.downloader

import dev.dizyaa.dizgram.core.telegram.TelegramContext
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.drinkless.td.libcore.telegram.TdApi

class TelegramFileDownloadManager(
    private val context: TelegramContext,
): FileDownloadManager {

    private val fileIdToTypeMap: MutableMap<FileId, File.Type> = mutableMapOf()
    private val flowUpdatedFile: Flow<File> = context.getUpdatesFlow<TdApi.UpdateFile>()
        .map { it.file.toDomain() }

    override fun getDownloadedFileFlow(type: File.Type, allowUnknownType: Boolean): Flow<File> =
        flowUpdatedFile
            .map {
                it.copy(type = fileIdToTypeMap[it.id] ?: File.Type.Unknown)
            }
            .filter {
                it.type == type || (allowUnknownType && it.type == File.Type.Unknown)
            }

    override suspend fun downloadById(id: FileId, type: File.Type) {
        fileIdToTypeMap[id] = type

        context.execute<TdApi.File>(
            TdApi.DownloadFile(
                id.value,
                16,
                0,
                0,
                false,
            )
        )
    }

    override suspend fun cancelDownloadById(id: FileId) {
        context.execute<TdApi.Ok>(
            TdApi.CancelDownloadFile(
                id.value,
                false
            )
        )
    }
}