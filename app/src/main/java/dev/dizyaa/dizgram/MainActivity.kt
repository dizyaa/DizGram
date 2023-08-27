package dev.dizyaa.dizgram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                AppUi()
            }
        }
    }
}

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme {
        content()
    }
}
