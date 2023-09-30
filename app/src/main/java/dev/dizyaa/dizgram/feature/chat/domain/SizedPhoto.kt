package dev.dizyaa.dizgram.feature.chat.domain

data class SizedPhoto(
    val miniThumbnail: MiniThumbnail?,
    val big: File?,
    val small: File?,
)

class MiniThumbnail(
    val data: ByteArray
)