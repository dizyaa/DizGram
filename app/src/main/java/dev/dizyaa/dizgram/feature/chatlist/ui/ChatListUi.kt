package dev.dizyaa.dizgram.feature.chatlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import coil.compose.rememberAsyncImagePainter
import dev.dizyaa.dizgram.core.uihelpers.SIDE_EFFECTS_KEY
import dev.dizyaa.dizgram.core.uihelpers.toImageRequestData
import dev.dizyaa.dizgram.feature.chat.domain.MessageContent
import dev.dizyaa.dizgram.feature.chatlist.ui.model.ChatCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ChatListDestination(
    navController: NavController,
) {
    val viewModel: ChatListViewModel = koinViewModel()

    ChatListUi(
        onNavigation = {
            when (it) {
                is ChatListContract.Effect.Navigation.ChatRequired -> {
                    navController.navigate("chat/${it.chatId.value}")
                }
            }
        },
        onEvent = { viewModel.setEvent(it) },
        stateFlow = viewModel.state,
        effectFlow = viewModel.effect,
    )
}

@Composable
fun ChatListUi(
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

    val chatListState = rememberScalingLazyListState()
    Scaffold(
        positionIndicator = {
            PositionIndicator(scalingLazyListState = chatListState)
        }
    ) {
        if (state.chatList.isNotEmpty()) {
            ChatList(
                list = state.chatList,
                onChatClick = { onEvent(ChatListContract.Event.SelectChat(it)) },
                modifier = Modifier.fillMaxWidth(),
                state = chatListState,
            )
        } else {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Empty list",
                    fontSize = 24.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ChatList(
    list: List<ChatCard>,
    onChatClick: (ChatCard) -> Unit,
    state: ScalingLazyListState,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    ScalingLazyColumn(
        modifier = modifier
            .onRotaryScrollEvent {
                coroutineScope.launch {
                    state.animateScrollBy(it.verticalScrollPixels)
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
        state = state,
        anchorType = ScalingLazyListAnchorType.ItemCenter,
    ) {
        items(
            list,
            key = {
                it.id.value
            }
        ) {
            ChatListItem(
                chat = it,
                onClick = { onChatClick(it) },
            )
        }
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
fun ChatListItem(
    chat: ChatCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Chip(
        label = {
            Column {
                Text(
                    text = chat.name,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body1,
                )
            }
        },
        secondaryLabel = {
            chat.lastMessage?.content?.let {
                Text(
                    text = buildAnnotatedString {
                        if (chat.lastMessageFromMyself) {
                            withStyle(
                                MaterialTheme.typography.caption2.copy(
                                    color = MaterialTheme.colors.onPrimary.copy(red = 1f)
                                ).toSpanStyle()
                            ) {
                                append("Me: ")
                            }
                        }

                        // TODO: move to VM
                        when (val content = it) {
                            is MessageContent.Text -> append(content.text)
                            is MessageContent.Photo -> append(content.text)
                            is MessageContent.Voice -> append(content.text)
                            is MessageContent.Unsupported -> Unit
                        }
                    },
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.body2,
                )
            }
        },
        onClick = onClick,
        icon = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = chat.sizedPhoto?.toImageRequestData(),
                    contentScale = ContentScale.Crop,
                ),
                contentDescription = null,
                modifier = Modifier
                    .size(ChipDefaults.LargeIconSize)
                    .clip(CircleShape)
            )
        },
        modifier = modifier
            .fillMaxWidth()
    )
}

@Preview
@Composable
private fun ChatListPreview() {
    ChatList(
        list = (0..10).map {
            ChatCard.fake(it.toLong())
        },
        onChatClick = { },
        state = rememberScalingLazyListState()
    )
}
