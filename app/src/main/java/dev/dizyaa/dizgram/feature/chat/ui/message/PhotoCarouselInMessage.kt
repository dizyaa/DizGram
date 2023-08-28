package dev.dizyaa.dizgram.feature.chat.ui.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.MaterialTheme
import coil.compose.AsyncImage
import dev.dizyaa.dizgram.core.uihelpers.toImageRequestData
import dev.dizyaa.dizgram.feature.chat.domain.File

@Composable
fun PhotoCarouselInMessage(
    fileList: List<File>
) {
    when (fileList.size) {
        0 -> Unit
        1 -> SinglePhoto(
            file = fileList.first()
        )
        else -> MultiPhoto(
            fileList = fileList
        )
    }
}

@Composable
private fun MultiPhoto(
    fileList: List<File>
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier,
    ) {
        items(fileList) {
            PhotoItem(file = it)
        }
    }
}

@Composable
private fun SinglePhoto(
    file: File,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        AsyncImage(
            model = file.toImageRequestData(),
            contentDescription = null
        )
    }
}

@Composable
private fun PhotoItem(
    file: File,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(100.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        AsyncImage(
            model = file.toImageRequestData(),
            contentDescription = null
        )
    }
}