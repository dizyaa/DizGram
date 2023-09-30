package dev.dizyaa.dizgram.feature.chat.domain


data class File(
    val id: FileId,
    val size: Int,
    val localFile: LocalFile,
    val remoteFile: RemoteFile,
)

data class LocalFile(
    val path: String,
    val isDownloadingActive: Boolean,
    val isDownloadingCompleted: Boolean,
    val canBeDownloaded: Boolean,
)

data class RemoteFile(
    val id: String,
)

val LocalFile.needBeDownloaded
    get() = !this.isDownloadingActive && this.canBeDownloaded && !this.isDownloadingCompleted

val File.sizeIsUnknown: Boolean
    get() = this.size == 0

data class FileId(val value: Int)