package dev.dizyaa.dizgram.feature.chatlist.domain

import androidx.compose.runtime.Stable

@Stable
data class ChatFilter(
    val id: Int,
    val title: String,
) {
    companion object {
        val Main = ChatFilter(
            id = -1,
            title = "All",
        )
    }
}