package dev.dizyaa.dizgram.feature.chat.ui

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.ScalingLazyListAnchorType
import androidx.wear.compose.material.ScalingLazyListState
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import dev.dizyaa.dizgram.AppTheme
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChatDestination(
    navController: NavController,
    chatId: ChatId,
) {
    val viewModel = koinViewModel<ChatViewModel>() {
        parametersOf(chatId)
    }

    ChatUi(
        onNavigation = {

        },
        onEvent = { viewModel.setEvent(it) },
        state = viewModel.state.collectAsState().value,
        effectFlow = viewModel.effect,
    )
}

@Composable
fun ChatUi(
    state: ChatContract.State,
    effectFlow: Flow<ChatContract.Effect>,
    onNavigation: (ChatContract.Effect.Navigation) -> Unit,
    onEvent: (ChatContract.Event) -> Unit,
) {
    val scalingLazyListState = rememberScalingLazyListState()

    Scaffold(
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = scalingLazyListState,
                reverseDirection = true,
            )
        },
        timeText = {
            Text(text = state.chatTitle)
        }
    ) {
        MessageList(
            list = state.messages,
            onMessageClick = { },
            state = scalingLazyListState
        )
    }
}

@Composable
private fun MessageList(
    list: List<MessageCard>,
    onMessageClick: (MessageCard) -> Unit,
    state: ScalingLazyListState,
    modifier: Modifier = Modifier
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
        reverseLayout = true,
    ) {
        items(
            list,
            key = {
                it.id.value
            }
        ) {
            MessageListItem(
                messageCard = it,
                onClick = { onMessageClick(it) }
            )
        }
    }
    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun MessageListItem(
    messageCard: MessageCard,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Chip(
            label = {
                Text(
                    text = messageCard.contentText,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            colors = if (messageCard.fromMe) {
                ChipDefaults.chipColors()
            } else {
                ChipDefaults.outlinedChipColors()
            },
            modifier = Modifier
                .align(
                    if (messageCard.fromMe) {
                        Alignment.TopEnd
                    } else {
                        Alignment.TopStart
                    }
                )
        )
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun ChatUiPreview() {
    AppTheme {
        ChatUi(
            state = ChatContract.State.mock(),
            effectFlow = emptyFlow(),
            onNavigation = { },
            onEvent = { },
        )
    }
}