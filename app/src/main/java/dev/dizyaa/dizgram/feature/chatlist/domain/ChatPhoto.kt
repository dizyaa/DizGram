package dev.dizyaa.dizgram.feature.chatlist.domain

data class ChatPhoto(
    val thumbnail: Photo?,
    val big: Photo?,
    val small: Photo?,
)

sealed class Photo {
    class Bytes(val array: ByteArray) : Photo()
    class Path(val path: String) : Photo()
}
