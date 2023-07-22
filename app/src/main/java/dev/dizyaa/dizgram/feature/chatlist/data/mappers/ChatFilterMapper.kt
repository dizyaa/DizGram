package dev.dizyaa.dizgram.feature.chatlist.data.mappers

import dev.dizyaa.dizgram.feature.chatlist.domain.ChatFilter
import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.ChatFilterInfo.toDomain(): ChatFilter {
    return ChatFilter(
        id = id,
        title = title,
    )
}