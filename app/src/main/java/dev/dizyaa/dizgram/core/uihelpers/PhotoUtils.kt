package dev.dizyaa.dizgram.core.uihelpers

import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import java.io.File

fun ChatPhoto.toImageRequestData(allowBigPhoto: Boolean = false): Any? {
    return this.toPriorityPhoto(allowBigPhoto)?.toImageRequestData()
}

fun ChatPhoto.toPriorityPhoto(
    allowBigPhoto: Boolean,
): dev.dizyaa.dizgram.feature.chat.domain.File? {
    return when {
        allowBigPhoto && this.big != null-> this.big
        (this.small != null) && this.small.path.isNotEmpty() -> this.small
        (this.thumbnail != null) && (this.thumbnail.bytes?.isNotEmpty() == true) -> this.thumbnail
        else -> null
    }
}

fun dev.dizyaa.dizgram.feature.chat.domain.File.toImageRequestData(): Any? {
    return when {
        this.path.isNotEmpty() -> File(this.path)
        this.bytes?.isNotEmpty() == true -> this.bytes
        else -> null
    }
}