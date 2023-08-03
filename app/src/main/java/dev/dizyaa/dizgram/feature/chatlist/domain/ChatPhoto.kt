package dev.dizyaa.dizgram.feature.chatlist.domain

import androidx.compose.runtime.Stable

@Stable
data class ChatPhoto(
    val thumbnail: Photo?,
    val big: Photo?,
    val small: Photo?,
)

@Stable
data class Photo(
    val id: FileId,
    val path: String,
    val bytes: ByteArray?,
    val needToDownload: Boolean,
) {
    companion object {
        fun fake() = Photo(
            id = FileId(-1),
            path = "/kek",
            bytes = ByteArray(0),
            needToDownload = false,
        )
    }
}

@Stable
data class FileId(val value: Int)