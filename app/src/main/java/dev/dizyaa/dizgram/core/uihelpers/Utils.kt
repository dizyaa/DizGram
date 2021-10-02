package dev.dizyaa.dizgram.core.uihelpers

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun isSmallWearableDevice(): Boolean {
    return LocalConfiguration.current.screenWidthDp.dp < 200.dp
}