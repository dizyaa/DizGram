package dev.dizyaa.dizgram.feature.chat.ui.message.content

import androidx.compose.runtime.Composable
import dev.dizyaa.dizgram.feature.chat.ui.PlayingVoiceNoteWrapper
import dev.dizyaa.dizgram.feature.chat.ui.message.BaseMessage
import dev.dizyaa.dizgram.feature.chat.ui.message.PhotoCarouselInMessage
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard

@Composable
fun MessageCardUi(
    messageCard: MessageCard,
    playingVoiceNoteWrapper: PlayingVoiceNoteWrapper,
) {
    BaseMessage(
        text = messageCard.text,
        fromUser = messageCard.fromMe,
        topContent = {
            PhotoCarouselInMessage(
                fileList = messageCard.files
            )

            messageCard.voiceNote?.let {
                VoiceContent(
                    playingStatus = messageCard.playingStatus,
                    voiceNote = it,
                    playOnClick = {
                        playingVoiceNoteWrapper.play(messageCard.id, it)
                    },
                    pauseOnClick = {
                        playingVoiceNoteWrapper.pause(messageCard.id, it)
                    },
                    downloadOnClick = {
                        playingVoiceNoteWrapper.download(messageCard.id, it)
                    },
                    stopDownloadOnClick = {
                        playingVoiceNoteWrapper.stopDownload(messageCard.id, it)
                    },
                    downloadProgress = messageCard.progress,
                )
            }
        }
    )
}