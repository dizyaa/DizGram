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
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import com.linc.audiowaveform.AudioWaveform
import dev.dizyaa.dizgram.feature.chat.domain.VoiceNote
import dev.dizyaa.dizgram.feature.chat.ui.model.PlayingStatus
import dev.dizyaa.dizgram.feature.chat.ui.model.Progress
import dev.dizyaa.dizgram.feature.chat.ui.model.isDownloading
import dev.dizyaa.dizgram.feature.chat.ui.model.isPlaying


@Composable
fun VoiceContent(
    playingStatus: PlayingStatus,
    downloadProgress: Progress,
    voiceNote: VoiceNote,
    playOnClick: () -> Unit,
    pauseOnClick: () -> Unit,
    downloadOnClick: () -> Unit,
    stopDownloadOnClick: () -> Unit,
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
            ControlButton(
                playingStatus = playingStatus,
                playOnClick = playOnClick,
                pauseOnClick = pauseOnClick,
                downloadOnClick = downloadOnClick,
                stopDownloadOnClick = stopDownloadOnClick,
                downloadProgress = downloadProgress,
            )

            Waveform(
                wave = voiceNote.waveform,
                progress = Progress(0f),
                modifier = Modifier
                    .padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun ControlButton(
    playingStatus: PlayingStatus,
    playOnClick: () -> Unit,
    pauseOnClick: () -> Unit,
    downloadOnClick: () -> Unit,
    stopDownloadOnClick: () -> Unit,
    downloadProgress: Progress,
    modifier: Modifier = Modifier,
) {
    when (playingStatus) {
        PlayingStatus.Playing -> PlayButton(
            isPlaying = playingStatus.isPlaying,
            onClick = pauseOnClick,
        )
        PlayingStatus.Pause,
        PlayingStatus.Stop -> PlayButton(
            isPlaying = playingStatus.isPlaying,
            onClick = playOnClick,
        )
        PlayingStatus.Downloading -> DownloadButton(
            isDownloading = playingStatus.isDownloading,
            progress = downloadProgress,
            onClick = stopDownloadOnClick,
        )
        PlayingStatus.NeedDownload -> DownloadButton(
            isDownloading = playingStatus.isDownloading,
            progress = downloadProgress,
            onClick = downloadOnClick,
        )
    }
}

@Composable
fun DownloadButton(
    isDownloading: Boolean,
    progress: Progress,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .clip(CircleShape)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        if (isDownloading) {
            CircularProgressIndicator(
                progress = kotlin.math.max(0f, progress.value),
                indicatorColor = MaterialTheme.colors.primaryVariant,
                trackColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.5f),
                strokeWidth = 3.dp,
                modifier = Modifier
                    .size(32.dp)
            )
        } else {
            Box(
                modifier = modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .border(
                        color = MaterialTheme.colors.onPrimary,
                        width = 3.dp,
                        shape = CircleShape,
                    )
            )
        }

        Icon(
            imageVector = Icons.Filled.Download,
            contentDescription = "download",
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .size(20.dp)
        )
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
            .clickable {
                onClick()
            }
    ) {
        Icon(
            imageVector = when(isPlaying) {
                true -> Icons.Filled.Pause
                false -> Icons.Filled.PlayArrow
            },
            contentDescription = when(isPlaying) {
                true -> "pause"
                false -> "play"
            },
            tint = MaterialTheme.colors.onPrimary,
            modifier = Modifier
                .padding(4.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun Waveform(
    wave: List<Int>,
    progress: Progress,
    modifier: Modifier = Modifier,
) {
    AudioWaveform(
        amplitudes = wave,
        progress = progress.value,
        onProgressChange = {
            // TODO
        },
        waveformBrush = SolidColor(MaterialTheme.colors.onPrimary),
        progressBrush = SolidColor(Color.White),
        modifier = modifier
    )
}

@Preview(
    name = "Playing"
)
@Composable
private fun DownloadButtonPreview() {
    ControlButton(
        playingStatus = PlayingStatus.Playing,
        downloadProgress = Progress(0f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}

@Preview(
    name = "Downloading Start"
)
@Composable
private fun DownloadButtonPreview1() {
    ControlButton(
        playingStatus = PlayingStatus.Downloading,
        downloadProgress = Progress(0f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}


@Preview(
    name = "Downloading Half progress"
)
@Composable
private fun DownloadButtonPreview2() {
    ControlButton(
        playingStatus = PlayingStatus.Downloading,
        downloadProgress = Progress(0.5f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}

@Preview(
    name = "Downloading End progress"
)
@Composable
private fun DownloadButtonPreview3() {
    ControlButton(
        playingStatus = PlayingStatus.Downloading,
        downloadProgress = Progress(1f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}

@Preview(
    name = "Need download"
)
@Composable
private fun DownloadButtonPreview4() {
    ControlButton(
        playingStatus = PlayingStatus.NeedDownload,
        downloadProgress = Progress(0f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}

@Preview(
    name = "Stop"
)
@Composable
private fun DownloadButtonPreview5() {
    ControlButton(
        playingStatus = PlayingStatus.Stop,
        downloadProgress = Progress(0f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}


@Preview(
    name = "Pause"
)
@Composable
private fun DownloadButtonPreview6() {
    ControlButton(
        playingStatus = PlayingStatus.Pause,
        downloadProgress = Progress(0f),
        playOnClick = {},
        pauseOnClick = {},
        downloadOnClick = {},
        stopDownloadOnClick = {},
        modifier = Modifier,
    )
}