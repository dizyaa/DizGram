package dev.dizyaa.dizgram.feature.chat.data.mappers

import dev.dizyaa.dizgram.feature.chat.domain.MessageContent
import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import org.drinkless.td.libcore.telegram.TdApi
import timber.log.Timber


fun TdApi.MessageContent.toDomain(): MessageContent {
    return when (this.constructor) {
        TdApi.MessageText.CONSTRUCTOR -> {
            val messageContent = (this as TdApi.MessageText)
            val text = messageContent.text.text
            val webPageUrl = messageContent.webPage?.url
            MessageContent.Text(text, webPageUrl)
        }
        TdApi.MessagePhoto.CONSTRUCTOR -> {
            val content = (this as TdApi.MessagePhoto)
            val text = content.caption.text

            //TODO: add sizes
            val photo = content.photo.sizes.maxBy { it.photo.size }.photo.toDomain()

            Timber.d(content.toString())

            MessageContent.Photo(
                file = photo,
                text = text,
            )
        }
        TdApi.MessageVoiceNote.CONSTRUCTOR -> {
            with (this as TdApi.MessageVoiceNote) {
                MessageContent.Voice(
                    text = caption.text,
                    isListened = isListened,
                    voice = voiceNote.toDomain(),
                )
            }
        }
//        TdApi.MessageAudio.CONSTRUCTOR -> {
//            val audioUrl = (this as TdApi.MessageAudio).audio.audio.
//            MessageContent.Audio(audioUrl)
//        }
//        TdApi.MessageDocument.CONSTRUCTOR -> {
//            val documentUrl = (this as TdApi.MessageDocument).document.document.url
//            MessageContent.Document(documentUrl)
//        }
//        TdApi.MessageVideo.CONSTRUCTOR -> {
//            val videoUrl = (this as TdApi.MessageVideo).video.video.url
//            MessageContent.Video(videoUrl)
//        }
//        TdApi.MessageSticker.CONSTRUCTOR -> {
//            val stickerUrl = (this as TdApi.MessageSticker).sticker.sticker.thumbnail.remote.fileId
//            MessageContent.Sticker(stickerUrl)
//        }
//        TdApi.MessageExpiredPhoto.CONSTRUCTOR -> {
//            val expiredPhotoUrl = (this as TdApi.MessageExpiredPhoto).photo.photos[0].sizes[0].photo.remote.fileId
//            MessageContent.ExpiredPhoto(expiredPhotoUrl)
//        }
//        TdApi.MessageAnimation.CONSTRUCTOR -> {
//            val animationUrl = (this as TdApi.MessageAnimation).animation
//            MessageContent.Animation(animationUrl)
//        }
//        TdApi.MessageExpiredVideo.CONSTRUCTOR -> {
//            val expiredVideoUrl = (this as TdApi.MessageExpiredVideo).video.video.url
//            MessageContent.ExpiredVideo(expiredVideoUrl)
//        }
//        TdApi.MessageVideoNote.CONSTRUCTOR -> {
//            val videoNoteUrl = (this as TdApi.MessageVideoNote).videoNote.video.thumbnail.remote.fileId
//            MessageContent.VideoNote(videoNoteUrl)
//        }
//        TdApi.MessageLocation.CONSTRUCTOR -> {
//            val latitude = (this as TdApi.MessageLocation).location.latitude
//            val longitude = this.location.longitude
//            MessageContent.Location(latitude, longitude)
//        }
//        TdApi.MessageVenue.CONSTRUCTOR -> {
//            val venueName = (this as TdApi.MessageVenue).venue.title
//            val latitude = (this as TdApi.MessageVenue).venue.location.latitude
//            val longitude = (this as TdApi.MessageVenue).venue.location.longitude
//            MessageContent.Venue(venueName, latitude, longitude)
//        }
//        TdApi.MessageContact.CONSTRUCTOR -> {
//            val contactName = (this as TdApi.MessageContact).contact.firstName
//            val phoneNumber = (this as TdApi.MessageContact).contact.phoneNumber
//            MessageContent.Contact(contactName, phoneNumber)
//        }
//        TdApi.MessageAnimatedEmoji.CONSTRUCTOR -> {
//            val emoji = (this as TdApi.MessageAnimatedEmoji).emoji
//            MessageContent.AnimatedEmoji(emoji)
//        }
//        TdApi.MessageDice.CONSTRUCTOR -> {
//            val diceValue = (this as TdApi.MessageDice).value
//            MessageContent.Dice(diceValue)
//        }
//        TdApi.MessageGame.CONSTRUCTOR -> {
//            val gameName = (this as TdApi.MessageGame).game.title
//            MessageContent.Game(gameName)
//        }
//        TdApi.MessagePoll.CONSTRUCTOR -> {
//            val question = (this as TdApi.MessagePoll).poll.question
//            val options = (this as TdApi.MessagePoll).poll.options.map { it.text }
//            MessageContent.Poll(question, options)
//        }
//        TdApi.MessageInvoice.CONSTRUCTOR -> {
//            val invoiceText = (this as TdApi.MessageInvoice).invoice.title
//            MessageContent.Invoice(invoiceText)
//        }
//        TdApi.MessageCall.CONSTRUCTOR -> {
//            val callDescription = (this as TdApi.MessageCall).discardReason
//            MessageContent.Call(callDescription)
//        }
//        TdApi.MessageVideoChatScheduled.CONSTRUCTOR -> {
//            val scheduledTime = (this as TdApi.MessageVideoChatScheduled).startDate
//            MessageContent.VideoChatScheduled(scheduledTime)
//        }
//        TdApi.MessageVideoChatStarted.CONSTRUCTOR -> {
//            val chatId = (this as TdApi.MessageVideoChatStarted).groupCallId
//            MessageContent.VideoChatStarted(chatId)
//        }
//        TdApi.MessageVideoChatEnded.CONSTRUCTOR -> {
//            val chatId = (this as TdApi.MessageVideoChatEnded).groupCallId
//            MessageContent.VideoChatEnded(chatId)
//        }
//        TdApi.MessageInviteVideoChatParticipants.CONSTRUCTOR -> {
//            val invitees = (this as TdApi.MessageInviteVideoChatParticipants).users.map { it.username }
//            MessageContent.InviteVideoChatParticipants(invitees)
//        }
//        TdApi.MessageBasicGroupChatCreate.CONSTRUCTOR -> {
//            val groupName = (this as TdApi.MessageBasicGroupChatCreate).title
//            MessageContent.BasicGroupChatCreate(groupName)
//        }
//        TdApi.MessageSupergroupChatCreate.CONSTRUCTOR -> {
//            val supergroupName = (this as TdApi.MessageSupergroupChatCreate).title
//            MessageContent.SupergroupChatCreate(supergroupName)
//        }
//        TdApi.MessageChatChangeTitle.CONSTRUCTOR -> {
//            val newTitle = (this as TdApi.MessageChatChangeTitle).title
//            MessageContent.ChatChangeTitle(newTitle)
//        }
//        TdApi.MessageChatChangePhoto.CONSTRUCTOR -> {
//            val newPhotoUrl = (this as TdApi.MessageChatChangePhoto).photo.photos[0].sizes[0].photo.remote.fileId
//            MessageContent.ChatChangePhoto(newPhotoUrl)
//        }
//        TdApi.MessageChatDeletePhoto.CONSTRUCTOR -> {
//            MessageContent.ChatDeletePhoto
//        }
//        TdApi.MessageChatAddMembers.CONSTRUCTOR -> {
//            val members = (this as TdApi.MessageChatAddMembers).memberUserIds.map { it.toString() }
//            MessageContent.ChatAddMembers(members)
//        }
//        TdApi.MessageChatJoinByLink.CONSTRUCTOR -> {
//            val link = (this as TdApi.MessageChatJoinByLink).inviteLink
//            MessageContent.ChatJoinByLink(link)
//        }
//        TdApi.MessageChatJoinByRequest.CONSTRUCTOR -> {
//            val userId = (this as TdApi.MessageChatJoinByRequest).userId
//            MessageContent.ChatJoinByRequest(userId)
//        }
//        TdApi.MessageChatDeleteMember.CONSTRUCTOR -> {
//            val userId = (this as TdApi.MessageChatDeleteMember).userId
//            MessageContent.ChatDeleteMember(userId)
//        }
//        TdApi.MessageChatUpgradeTo.CONSTRUCTOR -> {
//            val supergroupId = (this as TdApi.MessageChatUpgradeTo).supergroupId
//            MessageContent.ChatUpgradeTo(supergroupId)
//        }
//        TdApi.MessageChatUpgradeFrom.CONSTRUCTOR -> {
//            val basicGroupId = (this as TdApi.MessageChatUpgradeFrom).title
//            MessageContent.ChatUpgradeFrom(basicGroupId)
//        }
//        TdApi.MessagePinMessage.CONSTRUCTOR -> {
//            val pinnedMessage = mapTdApiToMessage((this as TdApi.MessagePinMessage).message)
//            MessageContent.PinMessage(pinnedMessage)
//        }
//        TdApi.MessageScreenshotTaken.CONSTRUCTOR -> {
//            val screenshotUrl = (this as TdApi.MessageScreenshotTaken).screenshot.file.remote.fileId
//            MessageContent.ScreenshotTaken(screenshotUrl)
//        }
//        TdApi.MessageChatSetTheme.CONSTRUCTOR -> {
//            val theme = (this as TdApi.MessageChatSetTheme).themeName
//            MessageContent.ChatSetTheme(theme)
//        }
//        TdApi.MessageChatSetTtl.CONSTRUCTOR -> {
//            val ttl = (this as TdApi.MessageChatSetTtl).ttl
//            MessageContent.ChatSetTtl(ttl)
//        }
//        TdApi.MessageCustomServiceAction.CONSTRUCTOR -> {
//            val action = (this as TdApi.MessageCustomServiceAction).text
//            MessageContent.CustomServiceAction(action)
//        }
//        TdApi.MessageGameScore.CONSTRUCTOR -> {
//            val game = (this as TdApi.MessageGameScore).gameTitle
//            val score = (this as TdApi.MessageGameScore).score
//            MessageContent.GameScore(game, score)
//        }
//        TdApi.MessagePaymentSuccessful.CONSTRUCTOR -> {
//            val amount = (this as TdApi.MessagePaymentSuccessful).invoice.totalAmount / 100.0
//            val currency = (this as TdApi.MessagePaymentSuccessful).invoice.currency
//            MessageContent.PaymentSuccessful(amount, currency)
//        }
//        TdApi.MessagePaymentSuccessfulBot.CONSTRUCTOR -> {
//            val amount = (this as TdApi.MessagePaymentSuccessfulBot).totalAmount / 100.0
//            val currency = (this as TdApi.MessagePaymentSuccessfulBot).currency
//            val botName = (this as TdApi.MessagePaymentSuccessfulBot).invoice.title
//            MessageContent.PaymentSuccessfulBot(amount, currency, botName)
//        }
//        TdApi.MessageContactRegistered.CONSTRUCTOR -> {
//            val contactName = (this as TdApi.MessageContactRegistered).firstName
//            MessageContent.ContactRegistered(contactName)
//        }
//        TdApi.MessageWebsiteConnected.CONSTRUCTOR -> {
//            val website = (this as TdApi.MessageWebsiteConnected).domainName
//            MessageContent.WebsiteConnected(website)
//        }
//        TdApi.MessagePassportDataSent.CONSTRUCTOR -> {
//            val data = (this as TdApi.MessagePassportDataSent).info
//            MessageContent.PassportDataSent(data)
//        }
//        TdApi.MessagePassportDataReceived.CONSTRUCTOR -> {
//            val data = (this as TdApi.MessagePassportDataReceived).elements.joinToString { it.type }
//            MessageContent.PassportDataReceived(data)
//        }
//        TdApi.MessageProximityAlertTriggered.CONSTRUCTOR -> {
//            val distance = (this as TdApi.MessageProximityAlertTriggered).traveler.distance
//            MessageContent.ProximityAlertTriggered(distance)
//        }
        else -> MessageContent.Unsupported
    }
}
