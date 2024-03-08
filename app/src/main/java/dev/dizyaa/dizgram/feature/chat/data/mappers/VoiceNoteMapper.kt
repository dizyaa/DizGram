package dev.dizyaa.dizgram.feature.chat.data.mappers

import dev.dizyaa.dizgram.feature.chatlist.data.mappers.toDomain
import org.drinkless.td.libcore.telegram.TdApi.VoiceNote

fun VoiceNote.toDomain() = dev.dizyaa.dizgram.feature.chat.domain.VoiceNote(
    file = voice.toDomain(),
    duration = duration,
    waveform = waveform.toList().map { it.toInt() },
)