package dev.dizyaa.dizgram.feature.chat.data.mappers

import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.domain.MessageContent
import dev.dizyaa.dizgram.feature.chat.domain.MessageId
import org.drinkless.td.libcore.telegram.TdApi

fun TdApi.DraftMessage.toDomain(): InputMessage {
    return InputMessage(
        content = when(val input = this.inputMessageText) {
            is TdApi.InputMessageText ->
                MessageContent.Text(text = input.text.text, webPageUrl = null)

//            is TdApi.InputMessagePhoto ->
//                MessageContent.Photo(text = input.caption.text, file = input.photo.toDomain())

            else -> null
        },
        date = this.date,
        replyMessageId = MessageId(this.replyToMessageId)
    )
}

fun InputMessage.toTdApi(): TdApi.DraftMessage {
    return TdApi.DraftMessage(
        this.replyMessageId.value,
        this.date,
        when (content) {
            is MessageContent.Text -> TdApi.InputMessageText(
                TdApi.FormattedText(content.text, emptyArray()),
                false,
                content.text.isEmpty()
            )

            else -> null
        }
    )
}