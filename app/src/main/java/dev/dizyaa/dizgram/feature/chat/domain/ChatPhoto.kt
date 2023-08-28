package dev.dizyaa.dizgram.feature.chat.domain

import androidx.compose.runtime.Stable

@Stable
data class ChatPhoto(
    val thumbnail: File?,
    val big: File?,
    val small: File?,
)

@Stable
data class File(
    val id: FileId,
    val path: String,
    val bytes: ByteArray?,
    val needToDownload: Boolean,
) {
    companion object {
        fun fake() = File(
            id = FileId(-1),
            path = "/kek",
            bytes = ByteArray(0),
            needToDownload = false,
        )
    }
}

@Stable
data class FileId(val value: Int)