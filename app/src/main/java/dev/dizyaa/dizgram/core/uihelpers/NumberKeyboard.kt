package dev.dizyaa.dizgram.core.uihelpers

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Backspace
import androidx.compose.material.icons.outlined.Send
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun NumberKeyboard(
    onClickKey: (Char) -> Unit,
    onDelete: () -> Unit,
    onSend: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(IntrinsicSize.Min)
    ) {
        val isSmall = isSmallWearableDevice()
        Row(
            modifier = Modifier
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onDelete() }
                    .padding(
                        vertical = 6.dp,
                    )
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Backspace,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(if (isSmall) 22.dp else 24.dp)
                        .align(Alignment.Center)
                )
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {onSend() }
                    .padding(
                        vertical = 6.dp,
                    )
                    .weight(1f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    tint = MaterialTheme.colors.primary,
                    contentDescription = null,
                    modifier = Modifier
                        .size(if (isSmall) 22.dp else 24.dp)
                        .align(Alignment.Center)
                )
            }
        }

        NRow(listOf('1', '2', '3', '4', '5'), onClickKey)
        NRow(listOf('6', '7', '8', '9', '0'), onClickKey)
    }
}

@Composable
private fun NRow(
    list: List<Char>,
    onClickKey: (Char) -> Unit,
) {
    Row() {
        list.forEach { item ->
            KButton(item = item.toString(), onClick = { onClickKey(item) })
        }
    }
}


@Composable
private fun KButton(
    item: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() }
    ) {
        val isSmall = isSmallWearableDevice()

        Text(
            text = item,
            color = MaterialTheme.colors.onBackground,
            fontSize = if (isSmall) 20.sp else 24.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(
                    horizontal = if (isSmall) 8.dp else 10.dp,
                    vertical = 4.dp,
                )
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_LARGE_ROUND)
@Composable
private fun Preview() {
    Box(Modifier.fillMaxSize()) {
        NumberKeyboard(
            onClickKey = {},
            modifier = Modifier.align(Alignment.Center),
            onDelete = {},
            onSend = {},
        )
    }
}

@Preview(showSystemUi = true, device = Devices.WEAR_OS_SMALL_ROUND)
@Composable
private fun Preview1() {
    Box(Modifier.fillMaxSize()) {
        NumberKeyboard(
            onClickKey = {},
            modifier = Modifier.align(Alignment.Center),
            onDelete = {},
            onSend = {},
        )
    }
}