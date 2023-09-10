package dev.dizyaa.dizgram.feature.chat.ui.message.content

import androidx.compose.runtime.Composable
import dev.dizyaa.dizgram.feature.chat.ui.message.BaseMessage
import dev.dizyaa.dizgram.feature.chat.ui.message.PhotoCarouselInMessage
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard

@Composable
fun MessageCardUi(
    messageCard: MessageCard,
    onClick: () -> Unit,
) {
    BaseMessage(
        text = messageCard.text,
        fromUser = messageCard.fromMe,
        topContent = {
            PhotoCarouselInMessage(
                fileList = messageCard.files
            )
        }
    )
}