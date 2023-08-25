package dev.dizyaa.dizgram.feature.chat.domain

sealed class MessageUpdateType {
    data class SendStatus(val status: SendingStatus) : MessageUpdateType()
    data class Content(val content: MessageContent) : MessageUpdateType()
    data class IsEdited(val isEdited: Boolean) : MessageUpdateType()
}