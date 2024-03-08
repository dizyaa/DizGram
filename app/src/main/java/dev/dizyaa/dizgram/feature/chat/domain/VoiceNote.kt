package dev.dizyaa.dizgram.feature.chat.domain

data class VoiceNote(
    val file: File,
    val duration: Int,
    val waveform: List<Int>,
)