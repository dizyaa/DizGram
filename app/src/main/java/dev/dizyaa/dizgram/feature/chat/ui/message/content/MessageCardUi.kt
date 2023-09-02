package dev.dizyaa.dizgram.feature.chat.ui.message.content

import androidx.compose.runtime.Composable
import dev.dizyaa.dizgram.feature.chat.ui.message.BaseMessage
import dev.dizyaa.dizgram.feature.chat.ui.message.PhotoCarouselInMessage
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCardType

@Composable
fun MessageCardUi(
    messageCard: MessageCard,
    onClick: () -> Unit,
) {
    val content = messageCard.type as? MessageCardType.WithMedia ?: return

    BaseMessage(
        text = content.text,
        fromUser = messageCard.fromMe,
        topContent = {
            PhotoCarouselInMessage(
                fileList = content.files
            )
        }
    )
}