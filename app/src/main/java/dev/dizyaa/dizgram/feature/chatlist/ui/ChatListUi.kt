package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import dev.dizyaa.dizgram.feature.chatlist.domain.Chat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatListDestination(
    navController: NavController,
) {
    val viewModel: ChatListViewModel = koinViewModel()

    ChatList(
        onNavigation = {

        },
        onEvent = { viewModel.setEvent(it) },
        stateFlow = viewModel.state,
        effectFlow = viewModel.effect,
    )
}

@Composable
fun ChatList(
    stateFlow: StateFlow<ChatListContract.State>,
    effectFlow: Flow<ChatListContract.Effect>,
    onNavigation: (ChatListContract.Effect.Navigation) -> Unit,
    onEvent: (ChatListContract.Event) -> Unit,
) {
    val state by stateFlow.collectAsState()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect {
            when (it) {
                is ChatListContract.Effect.Navigation -> onNavigation(it)
                is ChatListContract.Effect.ShowError -> { }
            }
        }
    }

    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth(),
        anchorType = ScalingLazyListAnchorType.ItemCenter,
    ) {
        items(state.chatList) {
            ChatListItem(
                chat = it,
                onClick = {
                    onEvent(ChatListContract.Event.SelectChat(it))
                }
            )
        }
    }
}

@Composable
fun ChatListItem(
    chat: Chat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        label = {
            Text(
                text = chat.name,
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
