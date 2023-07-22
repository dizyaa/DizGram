package dev.dizyaa.dizgram.feature.chatlist.data.mappers

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chatlist.domain.Message
import dev.dizyaa.dizgram.feature.chatlist.domain.MessageId
import dev.dizyaa.dizgram.feature.chatlist.domain.Photo
import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.Chat.toDomain(): Chat {
    return Chat(
        id = ChatId(id),
        lastMessage = lastMessage?.let { message ->
            Message(
                id = MessageId(message.id),
                chatId = ChatId(message.chatId)
            )
        },
        name = title,
        photo = photo?.toDomain(),
    )
}

fun TdApi.ChatPhotoInfo.toDomain(): ChatPhoto {
    return ChatPhoto(
        thumbnail = this.minithumbnail?.let { Photo.Bytes(it.data) },
        big = Photo.Path(this.big.local.path),
        small = Photo.Path(this.small.local.path),
    )
}