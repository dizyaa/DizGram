package dev.dizyaa.dizgram.feature.chat.ui.message.content

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import com.linc.audiowaveform.AudioWaveform
import dev.dizyaa.dizgram.feature.chat.domain.VoiceNote


@Composable
fun VoiceContent(
    isPlaying: Boolean,
    voiceNote: VoiceNote,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .padding(end = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            PlayButton(
                isPlaying = isPlaying,
                onClick = onClick,
                modifier = Modifier
            )

            Waveform(
                wave = voiceNote.waveform,
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun PlayButton(
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(CircleShape)
            .border(
                color = MaterialTheme.colors.onPrimary,
                width = 3.dp,
                shape = CircleShape,
            )
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = when(isPlaying) {
                true -> Icons.Filled.Pause
                false -> Icons.Filled.PlayArrow
            },
            contentDescription = null,
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun Waveform(
    wave: List<Byte>,
    modifier: Modifier = Modifier,
) {
    AudioWaveform(
        amplitudes = wave.map { it.toInt() },
        progress = 0.3f,
        onProgressChange = {

        },
        waveformBrush = SolidColor(MaterialTheme.colors.onPrimary),
        progressBrush = SolidColor(Color.White),
        modifier = modifier
    )
}