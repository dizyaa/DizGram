package dev.dizyaa.dizgram.core.utils

fun <F,T> Iterable<F>.mapFirst(block: (F) -> T?): T? {
    for (e in this) {
        block(e)?.let { return it }
    }
    return null
}