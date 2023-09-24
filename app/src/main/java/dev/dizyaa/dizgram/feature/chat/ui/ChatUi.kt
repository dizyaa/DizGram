package dev.dizyaa.dizgram.feature.chat.ui

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.foundation.AnchorType
import androidx.wear.compose.foundation.CurvedLayout
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListAnchorType
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.curvedText
import dev.dizyaa.dizgram.AppTheme
import dev.dizyaa.dizgram.core.uihelpers.TextRemoteInput
import dev.dizyaa.dizgram.feature.chat.domain.ChatId
import dev.dizyaa.dizgram.feature.chat.domain.InputMessage
import dev.dizyaa.dizgram.feature.chat.ui.message.content.MessageCardUi
import dev.dizyaa.dizgram.feature.chat.ui.message.content.MessageInputUi
import dev.dizyaa.dizgram.feature.chat.ui.message.content.MessageUnsupportedUi
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCard
import dev.dizyaa.dizgram.feature.chat.ui.model.MessageCardType
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

    LaunchedEffect(Unit) {
        effectFlow.collect {
            when (it) {
                is ChatContract.Effect.Navigation -> onNavigation(it)
                else -> Unit
            }
        }
    }

    Scaffold(
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = scalingLazyListState,
                reverseDirection = true,
            )
        },
        timeText = {
            CurvedLayout(
                anchorType = AnchorType.Center
            ) {
                curvedText(text = state.chatTitle)
            }
        }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            MessageList(
                list = state.messages,
                onMessageClick = { },
                state = scalingLazyListState,
                onNextPageRequire = {
                    onEvent(ChatContract.Event.NextPageRequired)
                },
                inputMessage = state.inputTextMessage,
                onSendMessageClick = {
                    onEvent(ChatContract.Event.SendMessageClick)
                }
            )

            if (state.canSendMessage) {
                TextRemoteInput(
                    onValueChange = {
                        onEvent(ChatContract.Event.ChangeInputTextMessage(it))
                    },
                    labelRemote = "Message",
                    remoteInputKey = "message_key",
                    isEmojisAllowed = true,
                    inputActionType = EditorInfo.IME_ACTION_SEND,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                )
            }
        }
    }
}

@Composable
private fun MessageList(
    list: List<MessageCard>,
    inputMessage: InputMessage?,
    onMessageClick: (MessageCard) -> Unit,
    onNextPageRequire: () -> Unit,
    onSendMessageClick: () -> Unit,
    state: ScalingLazyListState,
    modifier: Modifier = Modifier
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

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
        contentPadding = PaddingValues(vertical = (screenHeight / 4))
    ) {
        item {
            if (inputMessage != null) {
                MessageInputUi(
                    inputMessage = inputMessage,
                    onClick = { },
                    onSendMessageClick = onSendMessageClick,
                )
            }
        }

        items(
            list,
        ) {
            MessageListItem(
                messageCard = it,
                onClick = { onMessageClick(it) }
            )
        }
    }

    state.OnBottomReached(
        callback = {
            onNextPageRequire()
        },
        itemCountGap = 5
    )

    LaunchedEffect(Unit) { focusRequester.requestFocus() }
}

@Composable
private fun ScalingLazyListState.OnBottomReached(
    callback: () -> Unit,
    itemCountGap: Int,
) {
    val bottomReached by remember(this) {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null && lastVisibleItem.index >= layoutInfo.totalItemsCount - 1 - itemCountGap
        }
    }

    if (bottomReached) {
        LaunchedEffect(Unit) {
            callback()
        }
    }
}

@Composable
private fun MessageListItem(
    messageCard: MessageCard,
    onClick: () -> Unit,
) {
    when (messageCard.type) {
        MessageCardType.TextWithMedia -> MessageCardUi(
            messageCard = messageCard,
            onClick = onClick,
        )
        MessageCardType.Unsupported -> MessageUnsupportedUi(
            onClick = onClick,
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