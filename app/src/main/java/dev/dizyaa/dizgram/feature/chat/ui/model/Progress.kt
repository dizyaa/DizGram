package dev.dizyaa.dizgram.feature.chat.ui.model

data class Progress(
    var value: Float
) {
    init {
        value = value.coerceIn(0f, 1f)
    }
}