package dev.dizyaa.dizgram.feature.chat.ui.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
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
        modifier = Modifier
            .padding(8.dp),
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
            .heightIn(max = 100.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = file.toImageRequestData(),
            contentScale = ContentScale.Crop,
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
            .clip(RoundedCornerShape(8.dp))
    ) {
        AsyncImage(
            model = file.toImageRequestData(),
            contentScale = ContentScale.Crop,
            contentDescription = null
        )
    }
}