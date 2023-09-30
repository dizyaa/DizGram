package dev.dizyaa.dizgram

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import dev.dizyaa.dizgram.feature.auth.ui.AuthDestination
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.ui.ChatDestination
import dev.dizyaa.dizgram.feature.chatlist.ui.ChatListDestination

@Composable
fun AppUi(
    modifier: Modifier = Modifier,
) {
    val navController = rememberSwipeDismissableNavController()

    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "auth",
        modifier = modifier,
    ) {
        composable("auth") {
            AuthDestination(navController = navController)
        }

        composable("chatList") {
            ChatListDestination(navController = navController)
        }

        composable(Router.Chat.route) {
            ChatDestination(
                chatId = it.arguments?.getString("chatId")?.toLongOrNull()?.let { ChatId(it) }
                    ?: throw RuntimeException("Chat Id is not pass for chat/{chatId}!")
            )
        }
    }
}

