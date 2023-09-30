package dev.dizyaa.dizgram.feature.chatlist.data.mappers

import dev.dizyaa.dizgram.feature.chat.data.mappers.toDomain
import dev.dizyaa.dizgram.feature.chat.domain.AlbumMediaId
import dev.dizyaa.dizgram.feature.chat.domain.Chat
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.File
import dev.dizyaa.dizgram.feature.chat.domain.FileId
import dev.dizyaa.dizgram.feature.chat.domain.LocalFile
import dev.dizyaa.dizgram.feature.chat.domain.Message
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import dev.dizyaa.dizgram.feature.chat.domain.MessageSender
import dev.dizyaa.dizgram.feature.chat.domain.MiniThumbnail
import dev.dizyaa.dizgram.feature.chat.domain.RemoteFile
import dev.dizyaa.dizgram.feature.chat.domain.SendingStatus
import dev.dizyaa.dizgram.feature.chat.domain.SizedPhoto
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
        sizedPhoto = photo?.toDomain(),
        canSendMessage = this.permissions.canSendMessages,
        draftMessage = this.draftMessage?.toDomain(),
    )
}

fun TdApi.ChatPhotoInfo.toDomain(): SizedPhoto {
    return SizedPhoto(
        miniThumbnail = this.minithumbnail?.toDomain(),
        big = this.big.toDomain(),
        small = this.small.toDomain(),
    )
}

fun TdApi.Minithumbnail.toDomain(): MiniThumbnail {
    return MiniThumbnail(
        data = this.data,
    )
}

fun TdApi.File.toDomain(): File {
    return File(
        id = FileId(this.id),
        size = this.size,
        localFile = this.local.toDomain(),
        remoteFile = this.remote.toDomain(),
    )
}

fun TdApi.LocalFile.toDomain(): LocalFile {
    return LocalFile(
        path = this.path,
        isDownloadingActive = isDownloadingActive,
        isDownloadingCompleted = isDownloadingCompleted,
        canBeDownloaded = canBeDownloaded,
    )
}

fun TdApi.RemoteFile.toDomain(): RemoteFile {
    return RemoteFile(
        id = this.id,
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
