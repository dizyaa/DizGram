package dev.dizyaa.dizgram.feature.chatlist.domain

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