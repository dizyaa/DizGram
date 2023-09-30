package dev.dizyaa.dizgram.core.uihelpers

import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.SizedPhoto

fun SizedPhoto.toImageRequestData(
    allowBigPhoto: Boolean = false,
): Any? {
    if (allowBigPhoto) {
        this.big?.let {
            return it.toImageRequestData()
        }
    }

    this.small?.let {
        return it.toImageRequestData()
    }

    this.miniThumbnail?.let {
        return it.data
    }

    return null
}

fun File.toImageRequestData(): Any? {
    if (this.localFile.isDownloadingCompleted) {
        val path = this.localFile.path
        return when {
            path.startsWith(prefix ="http") -> path
            else -> java.io.File(path)
        }
    }

    return null
}