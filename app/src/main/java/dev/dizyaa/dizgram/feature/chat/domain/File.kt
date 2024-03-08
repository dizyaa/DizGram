package dev.dizyaa.dizgram.feature.chat.domain

import dev.dizyaa.dizgram.feature.chat.ui.model.Progress


data class File(
    val id: FileId,
    val size: Int,
    val localFile: LocalFile,
    val remoteFile: RemoteFile,
    val type: Type,
) {
    enum class Type {
        Photo, VoiceNote, Unknown,
    }
}

data class LocalFile(
    val path: String,
    val isDownloadingActive: Boolean,
    val isDownloadingCompleted: Boolean,
    val canBeDownloaded: Boolean,
    val downloadProgress: Progress,
)

data class RemoteFile(
    val id: String,
)

val LocalFile.needBeDownloaded
    get() = !this.isDownloadingActive && this.canBeDownloaded && !this.isDownloadingCompleted

val File.sizeIsUnknown: Boolean
    get() = this.size == 0

data class FileId(val value: Int)