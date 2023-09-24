package dev.dizyaa.dizgram.feature.chat.data.mappers

import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.domain.InputMessageContent
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.DraftMessage.toDomain(): InputMessage {
    return InputMessage(
        content = when(val input = this.inputMessageText) {
            is TdApi.InputMessageText ->
                InputMessageContent.Text(text = input.text.text)

            else -> null
        },
        replyMessageId = MessageId(this.replyToMessageId)
    )
}

fun InputMessage.toTdDraftMessage(): TdApi.DraftMessage {
    return TdApi.DraftMessage(
        this.replyMessageId?.value ?: 0,
        0,
        this.content?.toTdApi(),
    )
}

fun InputMessageContent.toTdApi(): TdApi.InputMessageContent {
    return when (this) {
        is InputMessageContent.Text -> TdApi.InputMessageText(
            TdApi.FormattedText(text, emptyArray()),
            false,
            text.isEmpty()
        )
    }
}