package dev.dizyaa.dizgram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colors = Colors(
                    primary = Color.DarkGray,
                    onPrimary = Color.White,
                    background = Color(0xFF171829)
                )
            ) {
                AppUi()
            }
        }
    }
}
