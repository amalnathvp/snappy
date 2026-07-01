package com.snappy.samsung.ui.collection

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.snappy.samsung.model.Screenshot
import com.snappy.samsung.viewmodel.CollectionViewModel
import com.snappy.samsung.viewmodel.SortMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    appName: String,
    viewModel: CollectionViewModel,
    onBackClick: () -> Unit,
    onScreenshotClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val screenshots by viewModel.screenshotsState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val sortMode by viewModel.sortMode.collectAsState()
    val favoritesOnly by viewModel.favoritesOnly.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()

    var columnCount by remember { mutableIntStateOf(3) }
    var showDatePicker by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = appName,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        modifier = modifier
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Search Input Row
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text("Search by filename…") },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Rounded.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    unfocusedBorderColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            // Filtering & Layout Control Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Filter Row: Sort Mode, Favorites, and selected Date
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Sort order indicator
                    FilterChip(
                        selected = true,
                        onClick = {
                            val nextMode = if (sortMode == SortMode.NEWEST_FIRST) {
                                SortMode.OLDEST_FIRST
                            } else SortMode.NEWEST_FIRST
                            viewModel.setSortMode(nextMode)
                        },
                        label = {
                            Text(
                                if (sortMode == SortMode.NEWEST_FIRST) "Newest" else "Oldest"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (sortMode == SortMode.NEWEST_FIRST) {
                                    Icons.Rounded.ArrowDownward
                                } else Icons.Rounded.ArrowUpward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (appName != "Favorites") {
                        FilterChip(
                            selected = favoritesOnly,
                            onClick = { viewModel.setFavoritesOnly(!favoritesOnly) },
                            label = { Text("Favorites") },
                            leadingIcon = {
                                Icon(
                                    imageVector = if (favoritesOnly) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                    contentDescription = null,
                                    tint = if (favoritesOnly) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (selectedDate != null) {
                        FilterChip(
                            selected = true,
                            onClick = { viewModel.setSelectedDate(null) },
                            label = { Text(formatDateLabel(selectedDate!!)) },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Cancel,
                                    contentDescription = "Clear Date",
                                    modifier = Modifier.size(16.dp)
                                )
                            },
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                // Action Row: Calendar and Grid Layout Options
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(
                            imageVector = Icons.Rounded.CalendarMonth,
                            contentDescription = "Filter by date",
                            tint = if (selectedDate != null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(
                        onClick = {
                            columnCount = when (columnCount) {
                                2 -> 3
                                3 -> 4
                                else -> 2
                            }
                        }
                    ) {
                        Icon(
                            imageVector = when (columnCount) {
                                2 -> Icons.Rounded.GridView
                                3 -> Icons.Rounded.ViewModule
                                else -> Icons.Rounded.ViewComfy
                            },
                            contentDescription = "Grid Sizing"
                        )
                    }
                }
            }

            // Screenshots Grid
            if (screenshots.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.PhotoLibrary,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.size(72.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No screenshots found",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(columnCount),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(screenshots, key = { it.id }) { screenshot ->
                        ScreenshotGridItem(
                            screenshot = screenshot,
                            onClick = { onScreenshotClick(screenshot.id) }
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.setSelectedDate(datePickerState.selectedDateMillis)
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
fun ScreenshotGridItem(
    screenshot: Screenshot,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.56f) // Standard 16:9 vertical ratio for phone screens
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        AsyncImage(
            model = screenshot.imageUri,
            contentDescription = screenshot.filename,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Small Favorite overlay badge
        if (screenshot.favorite) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.TopEnd)
                    .size(24.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = null,
                    tint = Color.Red,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

private fun formatDateLabel(timeMillis: Long): String {
    val sdf = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault())
    return sdf.format(java.util.Date(timeMillis))
}
