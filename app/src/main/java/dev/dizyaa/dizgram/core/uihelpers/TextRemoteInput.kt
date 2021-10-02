package dev.dizyaa.dizgram.core.uihelpers

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender

/***
 * @param inputActionType
 * following values: [EditorInfo.IME_ACTION_SEND], [EditorInfo.IME_ACTION_SEARCH],
 * [EditorInfo.IME_ACTION_DONE], [EditorInfo.IME_ACTION_GO]
 */
@Composable
fun TextRemoteInput(
    onValueChange: (String) -> Unit,
    labelRemote: String,
    remoteInputKey: String,
    modifier: Modifier = Modifier,
    isEmojisAllowed: Boolean = false,
    inputActionType: Int = EditorInfo.IME_ACTION_DONE,
) {
    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val result: CharSequence = results.getCharSequence(remoteInputKey) ?: return@let
                onValueChange(result.toString())
            }
        }
    Column(
        modifier = modifier,
    ) {
        Button(
            onClick = {
                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                val remoteInputs: List<RemoteInput> = listOf(
                    RemoteInput.Builder(remoteInputKey)
                        .setLabel(labelRemote)
                        .wearableExtender {
                            setEmojisAllowed(isEmojisAllowed)
                            setInputActionType(inputActionType)
                        }.build()
                )

                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)
                launcher.launch(intent)
            }
        ) {
            Icon(
                imageVector = Icons.Default.Keyboard,
                contentDescription = null,
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.WEAR_OS_LARGE_ROUND
)
@Composable
private fun Preview() {
    Box(Modifier.fillMaxSize()) {
        TextRemoteInput(
            onValueChange = {},
            labelRemote = "Label remote",
            remoteInputKey = "key",
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}