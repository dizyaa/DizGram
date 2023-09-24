package dev.dizyaa.dizgram.feature.chat.domain

sealed class InputMessageContent {
    data class Text(val text: String) : InputMessageContent()
}