package dev.dizyaa.dizgram.feature.chatlist.data.mappers

import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatId
import dev.dizyaa.dizgram.feature.chatlist.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chatlist.domain.FileId
import dev.dizyaa.dizgram.feature.chatlist.domain.Message
import dev.dizyaa.dizgram.feature.chatlist.domain.MessageId
import dev.dizyaa.dizgram.feature.chatlist.domain.Photo
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.MessageText

fun TdApi.Chat.toDomain(): Chat {
    return Chat(
        id = ChatId(id),
        lastMessage = lastMessage?.toDomain(),
        name = title,
        chatPhoto = photo?.toDomain(),
    )
}

fun TdApi.ChatPhotoInfo.toDomain(): ChatPhoto {
    return ChatPhoto(
        thumbnail = this.minithumbnail?.toDomainPhoto(),
        big = this.big.toDomainPhoto(),
        small = this.small.toDomainPhoto(),
    )
}

fun TdApi.Minithumbnail.toDomainPhoto(): Photo {
    return Photo(
        id = FileId(-1),
        path = "",
        bytes = this.data,
        needToDownload = false,
    )
}

fun TdApi.File.toDomainPhoto(): Photo {
    return Photo(
        id = FileId(this.id),
        path = this.local.path,
        bytes = null,
        needToDownload = this.local.needToDownload()
    )
}

fun TdApi.Message.toDomain(): Message? {
    return (this.content as? MessageText)?.let {
        Message(
            id = MessageId(this.id),
            chatId = ChatId(this.chatId),
            content = it.text.text,
        )
    }
}

fun TdApi.LocalFile.needToDownload() =
    !this.isDownloadingCompleted &&
            this.canBeDownloaded &&
            !this.isDownloadingActive
