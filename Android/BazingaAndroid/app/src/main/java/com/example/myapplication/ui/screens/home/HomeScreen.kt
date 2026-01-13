package com.example.myapplication.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.example.myapplication.data.BazingaRepository
import com.example.myapplication.data.ComicDto
import com.example.myapplication.data.resolveImageUrl
import com.example.myapplication.ui.UiState
import com.example.myapplication.ui.components.ErrorState
import com.example.myapplication.ui.components.LoadingState
import com.example.myapplication.ui.model.AuthState
import com.example.myapplication.ui.model.ComicViewData
import com.example.myapplication.ui.model.FilterState
import com.example.myapplication.ui.theme.BazingaMuted
import com.example.myapplication.ui.theme.BazingaRed
import com.example.myapplication.ui.theme.BazingaSurface
import com.example.myapplication.ui.theme.BazingaSurfaceAlt
import com.example.myapplication.ui.theme.BazingaTextMuted
import com.example.myapplication.ui.util.parseInstant

@Composable
fun HomeScreen(
    repository: BazingaRepository,
    authState: AuthState,
    onAddToCart: (ComicDto, String) -> Unit,
    onNavigateToSubscription: () -> Unit,
    onNavigateToWishlist: () -> Unit,
    wishlistComicIds: Set<Long>,
    onAddToWishlist: (Long) -> Unit,
    onRemoveFromWishlist: (Long) -> Unit
) {
    var comicsState by remember { mutableStateOf<UiState<List<ComicDto>>>(UiState.Loading) }
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var filterState by remember { mutableStateOf(FilterState()) }
    var digitalOnly by rememberSaveable { mutableStateOf(false) }
    var selectedComic by remember { mutableStateOf<ComicDto?>(null) }
    var viewAllTarget by rememberSaveable { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        comicsState = repository.fetchComics()
    }

    val comics = (comicsState as? UiState.Success)?.data.orEmpty()
    val allComics = comics.map {
        ComicViewData(
            id = it.id,
            title = it.title,
            creators = it.author?.ifBlank { "" } ?: "",
            series = it.series?.ifBlank { "" } ?: "",
            character = it.mainCharacter?.ifBlank { "" } ?: "",
            image = it.image,
            comicType = it.comicType ?: "PHYSICAL_COPY",
            createdAt = it.createdAt
        )
    }

    val seriesOptions = listOf("All Series") + allComics.map { it.series }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()
    val characterOptions = listOf("All Characters") + allComics.map { it.character }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()
    val creatorOptions = listOf("All Creators") + allComics.map { it.creators }
        .filter { it.isNotBlank() }
        .distinct()
        .sorted()

    val digitalExclusive = allComics.filter { it.comicType == "ONLY_DIGITAL" }

    val filteredComics = run {
        var filtered = if (digitalOnly) digitalExclusive else allComics
        if (searchQuery.isNotBlank()) {
            val query = searchQuery.lowercase()
            filtered = filtered.filter {
                it.title.lowercase().contains(query) ||
                    it.creators.lowercase().contains(query) ||
                    it.series.lowercase().contains(query) ||
                    it.character.lowercase().contains(query)
            }
        }
        if (filterState.value.isNotBlank() && !filterState.value.startsWith("All")) {
            filtered = when (filterState.type) {
                "series" -> filtered.filter { it.series == filterState.value }
                "character" -> filtered.filter { it.character == filterState.value }
                "creator" -> filtered.filter { it.creators.equals(filterState.value, ignoreCase = true) }
                else -> filtered
            }
        }
        filtered
    }

    val isFiltered = searchQuery.isNotBlank() || digitalOnly ||
        (filterState.value.isNotBlank() && !filterState.value.startsWith("All")) || viewAllTarget != null

    val filteredTitle = when {
        searchQuery.isNotBlank() -> "SEARCH RESULTS FOR \"${searchQuery.uppercase()}\""
        digitalOnly || viewAllTarget == "digital" -> "DIGITAL EXCLUSIVE"
        viewAllTarget == "all" -> "ALL COMICS"
        else -> "FILTERED RESULTS"
    }

    val newThisWeek = allComics
        .sortedByDescending { parseInstant(it.createdAt) }
        .take(6)
    val digitalRead = digitalExclusive.take(10)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { BazingaHeader(onWishlistClick = onNavigateToWishlist) }
            item { HeroBanner(onSubscribeClick = onNavigateToSubscription) }
            item { SearchBar(searchQuery = searchQuery, onSearchChange = { searchQuery = it }) }
            item {
                BrowseFilters(
                    seriesOptions = seriesOptions,
                    characterOptions = characterOptions,
                    creatorOptions = creatorOptions,
                    digitalOnly = digitalOnly,
                    onDigitalToggle = {
                        digitalOnly = it
                        viewAllTarget = null
                    },
                    filterState = filterState,
                    onFilterChange = { type, value ->
                        filterState = FilterState(type, value)
                        viewAllTarget = null
                    }
                )
            }
            item {
                when (comicsState) {
                    UiState.Loading -> LoadingState(message = "Loading comics...")
                    is UiState.Error -> ErrorState(message = (comicsState as UiState.Error).message)
                    is UiState.Success -> {
                        if (isFiltered) {
                            FilteredResults(
                                comics = filteredComics,
                                title = filteredTitle,
                                onClearFilters = {
                                    searchQuery = ""
                                    digitalOnly = false
                                    filterState = FilterState()
                                    viewAllTarget = null
                                },
                                onComicClick = { comic ->
                                    selectedComic = comics.find { it.id == comic.id }
                                }
                            )
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                ComicSection(
                                    title = "NEW THIS WEEK",
                                    comics = newThisWeek,
                                    showViewAll = false,
                                    onComicClick = { comic ->
                                        selectedComic = comics.find { it.id == comic.id }
                                    }
                                )
                                ComicSection(
                                    title = "DIGITAL EXCLUSIVE",
                                    comics = digitalRead,
                                    onViewAll = {
                                        viewAllTarget = "digital"
                                        digitalOnly = true
                                        searchQuery = ""
                                        filterState = FilterState()
                                    },
                                    onComicClick = { comic ->
                                        selectedComic = comics.find { it.id == comic.id }
                                    }
                                )
                                UnlimitedBanner(onSubscribeClick = onNavigateToSubscription)
                                ComicSection(
                                    title = "ALL COMICS",
                                    comics = allComics,
                                    onViewAll = {
                                        viewAllTarget = "all"
                                        digitalOnly = false
                                        searchQuery = ""
                                        filterState = FilterState()
                                    },
                                    onComicClick = { comic ->
                                        selectedComic = comics.find { it.id == comic.id }
                                    }
                                )
                            }
                        }
                    }
                }
            }
            item { BazingaFooter() }
        }
    }

    if (selectedComic != null) {
        ComicDetailDialog(
            comic = selectedComic!!,
            authState = authState,
            onDismiss = { selectedComic = null },
            onAddToCart = { purchaseType ->
                onAddToCart(selectedComic!!, purchaseType)
                selectedComic = null
            },
            isInWishlist = wishlistComicIds.contains(selectedComic!!.id),
            onToggleWishlist = {
                val comicId = selectedComic!!.id
                if (wishlistComicIds.contains(comicId)) {
                    onRemoveFromWishlist(comicId)
                } else {
                    onAddToWishlist(comicId)
                }
            }
        )
    }
}

@Composable
private fun SearchBar(searchQuery: String, onSearchChange: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Search titles, creators, or series",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        TextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = { Text("Search Bazinga comics") },
            leadingIcon = {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BazingaSurfaceAlt,
                unfocusedContainerColor = BazingaSurfaceAlt,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun FilterGroup(
    title: String,
    options: List<String>,
    filterState: FilterState,
    onFilterChange: (String, String) -> Unit,
    type: String
) {
    Column(modifier = Modifier.padding(top = 12.dp)) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = BazingaTextMuted
        )
        LazyRow(
            modifier = Modifier.padding(top = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(options) { label ->
                FilterChip(
                    selected = filterState.type == type && filterState.value == label,
                    onClick = { onFilterChange(type, label) },
                    label = { Text(text = label, fontSize = 12.sp) }
                )
            }
        }
    }
}

@Composable
private fun BrowseFilters(
    seriesOptions: List<String>,
    characterOptions: List<String>,
    creatorOptions: List<String>,
    digitalOnly: Boolean,
    onDigitalToggle: (Boolean) -> Unit,
    filterState: FilterState,
    onFilterChange: (String, String) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Browse by",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = digitalOnly,
                onClick = { onDigitalToggle(!digitalOnly) },
                label = { Text(text = "Digital Exclusive", fontSize = 12.sp) }
            )
            FilterChip(
                selected = !digitalOnly,
                onClick = { onDigitalToggle(false) },
                label = { Text(text = "All Comics", fontSize = 12.sp) }
            )
        }
        FilterGroup(
            title = "Series",
            options = seriesOptions,
            filterState = filterState,
            onFilterChange = onFilterChange,
            type = "series"
        )
        FilterGroup(
            title = "Characters",
            options = characterOptions,
            filterState = filterState,
            onFilterChange = onFilterChange,
            type = "character"
        )
        FilterGroup(
            title = "Creators",
            options = creatorOptions,
            filterState = filterState,
            onFilterChange = onFilterChange,
            type = "creator"
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilteredResults(
    comics: List<ComicViewData>,
    title: String,
    onClearFilters: () -> Unit,
    onComicClick: (ComicViewData) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${comics.size} comics",
                    fontSize = 12.sp,
                    color = BazingaTextMuted
                )
            }
            TextButton(onClick = onClearFilters) {
                Text(text = "Clear filters")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        BoxWithConstraints {
            val cardWidth = (maxWidth - 24.dp) / 3
            FlowRow(
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                comics.forEach { comic ->
                    ComicGridCard(
                        comic = comic,
                        modifier = Modifier.width(cardWidth),
                        onClick = { onComicClick(comic) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BazingaHeader(onWishlistClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "BAZINGA",
            color = BazingaRed,
            fontWeight = FontWeight.Black,
            letterSpacing = (-1).sp,
            fontSize = 22.sp
        )
        IconButton(onClick = onWishlistClick) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Wishlist",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
private fun HeroBanner(onSubscribeClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(BazingaSurfaceAlt, BazingaSurface)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "BAZINGA UNLIMITED",
                fontSize = 12.sp,
                color = BazingaTextMuted,
                fontWeight = FontWeight.SemiBold
            )
            Column {
                Text(
                    text = "Your universe of",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "comics starts here.",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Stream exclusive stories and new releases every week.",
                    fontSize = 14.sp,
                    color = BazingaTextMuted,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Button(
                    onClick = onSubscribeClick,
                    modifier = Modifier.padding(top = 12.dp)
                ) {
                    Text(text = "Start Reading")
                }
            }
        }
    }
}

@Composable
private fun ComicSection(
    title: String,
    comics: List<ComicViewData>,
    onComicClick: (ComicViewData) -> Unit,
    showViewAll: Boolean = true,
    onViewAll: (() -> Unit)? = null
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (showViewAll) {
                val viewAllModifier = if (onViewAll != null) {
                    Modifier.clickable { onViewAll() }
                } else {
                    Modifier
                }
                Text(
                    text = "View All",
                    fontSize = 12.sp,
                    color = BazingaTextMuted,
                    modifier = viewAllModifier
                )
            }
        }
        LazyRow(
            contentPadding = PaddingValues(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(comics) { comic ->
                ComicCard(comic = comic, onClick = { onComicClick(comic) })
            }
        }
    }
}

@Composable
private fun ComicCard(comic: ComicViewData, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BazingaSurface)
        ) {
            val resolvedImage = resolveImageUrl(comic.image)
            if (resolvedImage != null) {
                AsyncImage(
                    model = resolvedImage,
                    contentDescription = comic.title,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BazingaSurfaceAlt, BazingaSurface)
                            )
                        )
                )
            }
            if (comic.comicType == "ONLY_DIGITAL") {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BazingaRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Digital Exclusive",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Transparent)
                    .clip(RoundedCornerShape(12.dp))
            )
            IconButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.BottomEnd)
            ) {
                Icon(
                    imageVector = Icons.Filled.Book,
                    contentDescription = "Open comic",
                    tint = Color.White
                )
            }
        }
        Text(
            text = comic.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = comic.creators,
            fontSize = 11.sp,
            color = BazingaTextMuted
        )
    }
}

@Composable
private fun ComicGridCard(comic: ComicViewData, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(
        modifier = modifier
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .height(200.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(BazingaSurface)
        ) {
            val resolvedImage = resolveImageUrl(comic.image)
            if (resolvedImage != null) {
                AsyncImage(
                    model = resolvedImage,
                    contentDescription = comic.title,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(BazingaSurfaceAlt, BazingaSurface)
                            )
                        )
                )
            }
            if (comic.comicType == "ONLY_DIGITAL") {
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BazingaRed)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Digital Exclusive",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(vertical = 6.dp)
        ) {
            Text(text = "View Details", fontSize = 12.sp)
        }
    }
}

@Composable
private fun UnlimitedBanner(onSubscribeClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(BazingaRed)
            .padding(20.dp)
    ) {
        Column {
            Text(
                text = "Unlock Bazinga Unlimited",
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = "Get unlimited access to exclusive digital stories.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(top = 6.dp)
            )
            Button(
                onClick = onSubscribeClick,
                modifier = Modifier.padding(top = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = "Join Now", color = BazingaRed)
            }
        }
    }
}

@Composable
private fun BazingaFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "© 2024 BAZINGA COMICS",
            fontSize = 12.sp,
            color = BazingaTextMuted
        )
        Text(
            text = "All heroes. One universe.",
            fontSize = 11.sp,
            color = BazingaMuted,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ComicDetailDialog(
    comic: ComicDto,
    authState: AuthState,
    onDismiss: () -> Unit,
    onAddToCart: (String) -> Unit,
    isInWishlist: Boolean,
    onToggleWishlist: () -> Unit
) {
    val price = comic.price ?: 4.99
    val isDigitalExclusive = comic.comicType == "ONLY_DIGITAL"
    val subscriptionType = authState.subscriptionType?.lowercase()
    val isUnlimited = subscriptionType == "unlimited"
    val originalPrice = if (isUnlimited) price * 0.5 else price
    val digitalPrice = if (isUnlimited) 0.0 else price * 0.75
    var purchaseType by remember { mutableStateOf(if (isDigitalExclusive) "DIGITAL" else "ORIGINAL") }

    LaunchedEffect(comic.id) {
        purchaseType = if (isDigitalExclusive) "DIGITAL" else "ORIGINAL"
    }

    val selectedPrice = if (purchaseType == "DIGITAL") digitalPrice else originalPrice

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
        title = {
            Text(text = comic.title, fontWeight = FontWeight.Black, fontSize = 20.sp)
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val image = resolveImageUrl(comic.image)
                if (image != null) {
                    AsyncImage(
                        model = image,
                        contentDescription = comic.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
                Text(text = comic.author ?: "Bazinga Studios", fontSize = 12.sp, color = BazingaTextMuted)
                Text(
                    text = comic.description ?: "Discover the latest adventures from Bazinga Comics.",
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )
                if (isDigitalExclusive) {
                    Text(
                        text = "Digital Exclusive",
                        fontWeight = FontWeight.Bold,
                        color = BazingaRed,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (!isDigitalExclusive) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        PurchaseTypeCard(
                            title = "Original Copy",
                            subtitle = if (originalPrice == 0.0) "FREE WITH UNLIMITED" else "$${"%.2f".format(originalPrice)}",
                            isSelected = purchaseType == "ORIGINAL",
                            onClick = { purchaseType = "ORIGINAL" },
                            modifier = Modifier.weight(1f)
                        )
                        PurchaseTypeCard(
                            title = "Digital Copy",
                            subtitle = if (digitalPrice == 0.0) "FREE WITH UNLIMITED" else "$${"%.2f".format(digitalPrice)}",
                            isSelected = purchaseType == "DIGITAL",
                            onClick = { purchaseType = "DIGITAL" },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Back")
                }
                TextButton(
                    onClick = onToggleWishlist,
                    enabled = authState.token.isNotBlank()
                ) {
                    Icon(
                        imageVector = if (isInWishlist) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Wishlist"
                    )
                    Text(
                        text = if (isInWishlist) "Remove from wishlist" else "Add to wishlist",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                if (authState.token.isBlank()) {
                    TextButton(onClick = onDismiss) {
                        Text("Sign in to buy")
                    }
                } else {
                    TextButton(onClick = { onAddToCart(purchaseType) }) {
                        Text(
                            text = if (selectedPrice == 0.0) "Add to cart - Free" else "Add to cart - $${"%.2f".format(selectedPrice)}"
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun PurchaseTypeCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val background = if (isSelected) BazingaSurfaceAlt else BazingaSurface
    val borderColor = if (isSelected) BazingaRed else BazingaSurfaceAlt
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        Text(text = subtitle, fontSize = 11.sp, color = BazingaTextMuted)
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .height(2.dp)
                .fillMaxWidth()
                .background(borderColor)
        )
    }
}
