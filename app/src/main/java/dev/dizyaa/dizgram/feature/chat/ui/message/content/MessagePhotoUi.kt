package dev.dizyaa.dizgram.feature.chat.ui.message.content

import androidx.compose.runtime.Composable
import dev.dizyaa.dizgram.feature.chat.domain.MessageContent
import dev.dizyaa.dizgram.feature.chat.ui.message.BaseMessage
import dev.dizyaa.dizgram.feature.chat.ui.message.PhotoCarouselInMessage
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard

@Composable
fun MessagePhotoUi(
    messageCard: MessageCard,
    onClick: () -> Unit,
) {
    val content = (messageCard.content as? MessageContent.Photo) ?: return

    BaseMessage(
        text = content.text,
        topContent = {
            PhotoCarouselInMessage(
                fileList = content.fileList
            )
        }
    )
}