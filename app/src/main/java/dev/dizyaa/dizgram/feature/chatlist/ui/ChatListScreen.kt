package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListAnchorType
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import dev.dizyaa.dizgram.R
import dev.dizyaa.dizgram.core.uihelpers.SIDE_EFFECTS_KEY
import org.koin.androidx.compose.koinViewModel


@Composable
fun ChatListDestination(
    navController: NavController,
    viewModel: ChatListViewModel = koinViewModel()
) {
    ChatList(
        onNavigation = {
            when (it) {
                else -> {}
            }
        },
        viewModel = viewModel,
    )
}

@Composable
fun ChatList(
    onNavigation: (ChatListContract.Effect.Navigation) -> Unit,
    viewModel: ChatListViewModel,
    modifier: Modifier = Modifier,
) {
    val state = viewModel.state.collectAsState().value

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        viewModel.effect.collect {
            when (it) {
                is ChatListContract.Effect.Navigation -> onNavigation(it)
                is ChatListContract.Effect.ShowError -> { }
            }
        }
    }

    ScalingLazyColumn(
        modifier = modifier.fillMaxWidth(),
        anchorType = ScalingLazyListAnchorType.ItemCenter,
    ) {
        items(state.chatList) {
            ChatListItem(
                chatUi = it,
                onClick = {
                    viewModel.selectChat(it)
                }
            )
        }
    }
}

@Composable
fun ChatListItem(
    chatUi: ChatUi,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        label = {
            Text(
                text = chatUi.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(R.mipmap.ic_launcher),
                contentDescription = null,
                modifier = Modifier
                    .size(ChipDefaults.LargeIconSize)
                    .wrapContentSize(align = Alignment.Center)
            )
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

data class ChatUi(
    val image: String,
    val name: String,
    val lastMessage: String,
)
