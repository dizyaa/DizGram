package dev.dizyaa.dizgram.feature.chat.ui.message

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text

@Composable
fun BaseMessage(
    text: String,
    modifier: Modifier = Modifier,
    fromUser: Boolean = false,
    onClick: (() -> Unit)? = null,
    topContent: (@Composable () -> Unit)? = null,
    bottomContent: (@Composable () -> Unit)? = null,
) {
    val backgroundColor = when (fromUser) {
        true -> MaterialTheme.colors.primary
        false -> MaterialTheme.colors.secondary
    }
    val textColor = when (fromUser) {
        true -> MaterialTheme.colors.onPrimary
        false -> MaterialTheme.colors.onSecondary
    }
    val align = when (fromUser) {
        true -> Alignment.TopEnd
        false -> Alignment.TopStart
    }

    val maxLines = 3

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                enabled = onClick != null,
                onClick = { onClick?.invoke()}
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = if (fromUser) 16.dp else 0.dp,
                    end = if (fromUser) 0.dp else 16.dp,
                )
                .align(align)
                .clip(RoundedCornerShape(16.dp))
                .background(backgroundColor),
        ) {
            var expanded by remember {
                mutableStateOf(false)
            }
            var enabled by remember {
                mutableStateOf(false)
            }

            topContent?.invoke()

            if (text.isNotEmpty()) {
                AnimatedContent(
                    targetState = expanded,
                    label = "expandedText",
                ) { isExpanded ->
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        overflow = if (isExpanded) TextOverflow.Visible else TextOverflow.Ellipsis,
                        maxLines = if (isExpanded) Int.MAX_VALUE else maxLines,
                        color = textColor,
                        onTextLayout = {
                            enabled = (!isExpanded && !it.hasVisualOverflow).not()
                        },
                        modifier = Modifier
                            .padding(
                                horizontal = 16.dp,
                                vertical = 8.dp
                            )
                            .clickable(
                                indication = null,
                                interactionSource = MutableInteractionSource(),
                                enabled = enabled,
                                onClick = {
                                    expanded = !expanded
                                }
                            )
                    )
                }
            }

            bottomContent?.invoke()
        }
    }
}