package com.example.myapplication.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.ComicDto
import com.example.myapplication.data.resolveImageUrl
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaTextMuted

@Composable
fun ReaderScreen(repository: BazingaRepository, comicId: Long, onBack: () -> Unit) {
    var comicsState by remember { mutableStateOf<UiState<List<ComicDto>>>(UiState.Loading) }

    LaunchedEffect(comicId) {
        comicsState = repository.fetchComics()
    }

    val comic = (comicsState as? UiState.Success)?.data?.find { it.id == comicId }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Digital Read",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BazingaRed
                    )
                    Text(
                        text = comic?.title ?: "Bazinga Comic Issue",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = comic?.author ?: "Bazinga Studios",
                        fontSize = 12.sp,
                        color = BazingaTextMuted
                    )
                }
                TextButton(onClick = onBack) {
                    Text(text = "Back")
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Page 1",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = comic?.description
                            ?: "Explore the digital-first experience with exclusive panels and story beats.",
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }
            val image = resolveImageUrl(comic?.image)
            if (image != null) {
                AsyncImage(
                    model = image,
                    contentDescription = comic?.title ?: "Comic cover",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }
        }
    }
}
