package dev.dizyaa.dizgram.core.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

inline fun <reified A> Flow<*>.filteredMap(crossinline mapping: (A) -> Any): Flow<*> {
    return this
        .map {
            if (it is A) {
                mapping(it)
            } else {
                it
            }
        }
}