package dev.dizyaa.dizgram.feature.auth.ui

import android.view.inputmethod.EditorInfo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import dev.dizyaa.dizgram.core.uihelpers.NumberKeyboard
import dev.dizyaa.dizgram.core.uihelpers.SIDE_EFFECTS_KEY
import dev.dizyaa.dizgram.core.uihelpers.TextRemoteInput
import dev.dizyaa.dizgram.feature.auth.domain.AuthStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthDestination(
    navController: NavController,
) {
    val viewModel: AuthViewModel = koinViewModel()

    AuthScreen(
        onNavigation = {
            when (it) {
                is AuthContract.Effect.Navigation.ChatList -> {
                    navController.navigate("chatList") {
                        popUpTo(0)
                    }
                }
            }
        },
        onEvent = { viewModel.setEvent(it) },
        stateFlow = viewModel.state,
        effectFlow = viewModel.effect,
    )
}

@Composable
fun AuthScreen(
    stateFlow: StateFlow<AuthContract.State>,
    effectFlow: Flow<AuthContract.Effect>,
    onEvent: (AuthContract.Event) -> Unit,
    onNavigation: (AuthContract.Effect.Navigation) -> Unit,
) {
    val state by stateFlow.collectAsState()

    LaunchedEffect(SIDE_EFFECTS_KEY) {
        effectFlow.collect {
            when (it) {
                is AuthContract.Effect.Navigation -> onNavigation(it)
            }
        }
    }

    Scaffold {
        when (state.authStatus) {
            AuthStatus.WaitPhoneNumber -> PhoneNumber(
                onConfirm = { onEvent(AuthContract.Event.EnterByPhoneNumber(it)) }
            )
            AuthStatus.WaitCode -> Code(
                onConfirm = { onEvent(AuthContract.Event.LoginByCode(it)) }
            )
            AuthStatus.WaitPassword -> Password(
                onConfirm = { onEvent(AuthContract.Event.LoginByPassword(it)) }
            )
            AuthStatus.WaitOtherDeviceConfirmation -> OtherConfirmation()
            AuthStatus.WaitRegistration -> Registration()
            AuthStatus.Ready -> Ready()
            else -> Unit
        }
    }
}

@Composable
private fun PhoneNumber(
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenWithNumberKeyboard(
        title = "Enter phone number",
        onConfirm = onConfirm,
        modifier = modifier,
    )
}

@Composable
private fun Code(
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    ScreenWithNumberKeyboard(
        title = "Enter code",
        onConfirm = onConfirm,
        modifier = modifier,
    )
}

@Composable
private fun ScreenWithNumberKeyboard(
    title: String,
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {
            var value by remember {
                mutableStateOf("")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = value.ifBlank { title },
                    fontSize = 14.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                NumberKeyboard(
                    onClickKey = { char -> value += char },
                    onDelete = { value = value.dropLast(1) },
                    onSend = { onConfirm(value) }
                )
            }
        }
    }
}

@Composable
private fun Password(
    onConfirm: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember {
        mutableStateOf("")
    }
    Box(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ){
            Text(
                text = if (password.isBlank()) {
                    "Enter password"
                } else {
                    "*".repeat(password.length)
                },
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = modifier
            ) {
                TextRemoteInput(
                    onValueChange = {
                        password = it
                    },
                    labelRemote = "Password",
                    remoteInputKey = "input_ver_password",
                    inputActionType = EditorInfo.IME_ACTION_DONE,
                    modifier = Modifier,
                )

                Button(onClick = { onConfirm(password) }) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Composable
private fun OtherConfirmation(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Wait other device confirmation",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Registration(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Need registration. Pls get registration on mobile or desktop device",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun Ready(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Ready. Please wait",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun AuthPreview1() {
    Registration()
}

@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun AuthPreview2() {
    Password({})
}


@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun AuthPreview3() {
    OtherConfirmation()
}


@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun AuthPreview4() {
    Code({})
}


@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun AuthPreview5() {
    PhoneNumber({})
}


