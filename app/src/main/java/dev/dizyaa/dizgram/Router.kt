package dev.dizyaa.dizgram

// TODO: refactor this or remove
sealed class Router(
    val route: String,
) {
    object ChatList : Router("chatList")
    object Auth : Router("auth")
    object Chat : Router("chat/{chatId}")
}