package dev.dizyaa.dizgram.feature.chatlist.data.mappers

import dev.dizyaa.dizgram.feature.chat.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chat.domain.AlbumMediaId
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.ChatPhoto
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.MessageSender
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.chat.domain.toDomain
import dev.dizyaa.dizgram.feature.user.domain.UserId
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.MessageSenderChat
import org.drinkless.td.libcore.telegram.TdApi.MessageSenderUser
import org.drinkless.td.libcore.telegram.TdApi.MessageSendingStateFailed
import org.drinkless.td.libcore.telegram.TdApi.MessageSendingStatePending

fun TdApi.Chat.toDomain(): Chat {
    return Chat(
        id = ChatId(id),
        lastMessage = lastMessage?.toDomain(),
        name = title,
        chatPhoto = photo?.toDomain(),
        canSendMessage = this.permissions.canSendMessages,
        draftMessage = this.draftMessage?.toDomain(),
    )
}

fun TdApi.InputFile.toDomain(): File {
    return when (this) {
        is TdApi.InputFileRemote -> File(
            id = FileId(this.id.toInt()),
            path = "",
            bytes = null,
            needToDownload = true,
        )
        is TdApi.InputFileLocal -> File(
            id = FileId(-1),
            path = this.path,
            bytes = null,
            needToDownload = false,
        )
        is TdApi.InputFileId -> File(
            id = FileId(this.id),
            path = "",
            bytes = null,
            needToDownload = true,
        )
        is TdApi.InputFileGenerated -> File(
            id = FileId(-1),
            path = this.originalPath,
            bytes = null,
            needToDownload = false,
        )
        else -> throw RuntimeException("InputFile has not been mapped")
    }
}


fun TdApi.ChatPhotoInfo.toDomain(): ChatPhoto {
    return ChatPhoto(
        thumbnail = this.minithumbnail?.toDomainPhoto(),
        big = this.big.toDomainPhoto(),
        small = this.small.toDomainPhoto(),
    )
}

fun TdApi.Minithumbnail.toDomainPhoto(): File {
    return File(
        id = FileId(-1),
        path = "",
        bytes = this.data,
        needToDownload = false,
    )
}

fun TdApi.File.toDomainPhoto(): File {
    return File(
        id = FileId(this.id),
        path = this.local.path,
        bytes = null,
        needToDownload = this.local.needToDownload()
    )
}

fun TdApi.Message.toDomain(): Message {
    return Message(
        id = MessageId(this.id),
        chatId = ChatId(this.chatId),
        content = this.content.toDomain(),
        sender = this.senderId.toDomain(),
        isEdited = this.editDate >= this.date,
        isPinned = this.isPinned,
        status = this.sendingState.toDomain(),
        date = this.date,
        albumId = this.mediaAlbumId
            .takeIf { it > 0 }
            ?.let { AlbumMediaId(it) }
    )
}

fun TdApi.MessageSender.toDomain(): MessageSender{
    val id = when (this) {
        is MessageSenderChat -> {
            ChatId(this.chatId)
        }
        is MessageSenderUser -> {
            UserId(this.userId)
        }
        else -> throw RuntimeException("MessageSender is unknown!")
    }

    return MessageSender(
        senderId = id,
    )
}

fun TdApi.MessageSendingState?.toDomain(): SendingStatus {
    return when (this) {
        is MessageSendingStatePending -> SendingStatus.InProgress
        is MessageSendingStateFailed -> SendingStatus.Error
        else -> SendingStatus.Read
    }
}

fun TdApi.LocalFile.needToDownload() =
    !this.isDownloadingCompleted &&
            this.canBeDownloaded &&
            !this.isDownloadingActive
