package dev.dizyaa.dizgram.core.voice_player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import dev.dizyaa.dizgram.feature.chat.ui.model.VoiceNotePlayerState
import timber.log.Timber


class VoicePlayer(
    private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var path: String? = null

    fun updateState(state: VoiceNotePlayerState) {
        if (state.file != path) {
            reset()
            if (state.file != null) {
                playAudio(state.file)
            }
        }

        when (state.isPlaying) {
            true -> resume()
            false -> pause()
        }
    }

    private fun playAudio(path: String) {
        this.path = path
        mediaPlayer = MediaPlayer.create(context, Uri.parse(path)).apply {
            this.setOnInfoListener { mp, what, extra ->
                Timber
                    .tag("VoicePlayer")
                    .i(MediaPlayerInfo.fromInt(what).toString())

                true
            }
        }
    }

    private fun pause() {
        mediaPlayer?.pause()
    }

    private fun resume() {
        mediaPlayer?.start()
    }

    private fun reset() {
        mediaPlayer?.stop()
        mediaPlayer?.reset()
    }
}