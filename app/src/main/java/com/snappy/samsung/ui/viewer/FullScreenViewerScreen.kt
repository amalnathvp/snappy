package com.snappy.samsung.ui.viewer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.snappy.samsung.model.Screenshot
import com.snappy.samsung.viewmodel.ViewerUiState
import com.snappy.samsung.viewmodel.ViewerViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun FullScreenViewerScreen(
    viewModel: ViewerViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    var showDeleteConfirm by remember { mutableStateOf(false) }
    var screenshotToDelete by remember { mutableStateOf<Screenshot?>(null) }

    // Launcher for OS-mediated MediaStore delete permission dialog (API 30+)
    val deleteLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            screenshotToDelete?.let {
                viewModel.onDeleteConfirmed(it.id)
            }
        }
        screenshotToDelete = null
    }

    // Collect delete pending intents sent from viewModel
    LaunchedEffect(viewModel) {
        viewModel.deletePendingIntent.collect { pendingIntent ->
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent.intentSender).build()
            deleteLauncher.launch(intentSenderRequest)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        when (val state = uiState) {
            is ViewerUiState.Loading -> {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            is ViewerUiState.Success -> {
                val screenshots = state.screenshots
                val initialIndex = state.initialIndex

                if (screenshots.isEmpty()) {
                    LaunchedEffect(Unit) {
                        onBackClick()
                    }
                    return@Box
                }

                // Set up pager state dynamically (initialPage is bound directly on first instantiation)
                val pagerState = rememberPagerState(
                    initialPage = initialIndex,
                    pageCount = { screenshots.size }
                )

                val currentScreenshot = screenshots.getOrNull(pagerState.currentPage)

                Box(modifier = Modifier.fillMaxSize()) {
                    // Horizontal Pager for swipeable screenshots
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize(),
                        pageSpacing = 16.dp
                    ) { page ->
                        val screenshot = screenshots.getOrNull(page)
                        if (screenshot != null) {
                            ZoomableImage(
                                uri = screenshot.imageUri,
                                contentDescription = screenshot.filename
                            )
                        }
                    }

                    // Top Overlay Bar (Controls)
                    currentScreenshot?.let { screenshot ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.TopCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.8f),
                                            Color.Transparent
                                        )
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(
                                    onClick = onBackClick,
                                    modifier = Modifier.background(Color.Black.copy(alpha = 0.4f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                        contentDescription = "Back",
                                        tint = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = screenshot.filename,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "${formatEpochTime(screenshot.dateTaken)} • ${formatSize(screenshot.fileSize)}",
                                        color = Color.White.copy(alpha = 0.7f),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }

                    // Bottom Actions Bar
                    currentScreenshot?.let { screenshot ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .align(Alignment.BottomCenter)
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.85f)
                                        )
                                    )
                                )
                                .padding(bottom = 36.dp, top = 20.dp, start = 16.dp, end = 16.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                                    .padding(vertical = 12.dp, horizontal = 24.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Favorite Action
                                IconButton(onClick = { viewModel.toggleFavorite(screenshot) }) {
                                    Icon(
                                        imageVector = if (screenshot.favorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                                        contentDescription = "Favorite",
                                        tint = if (screenshot.favorite) Color.Red else Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Custom Collections Action
                                var showCollectionDialog by remember { mutableStateOf(false) }
                                IconButton(onClick = { showCollectionDialog = true }) {
                                    Icon(
                                        imageVector = Icons.Rounded.FolderOpen,
                                        contentDescription = "Custom Collections",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Open in external gallery application
                                IconButton(onClick = { openInGallery(context, screenshot) }) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                        contentDescription = "Open in Gallery",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Share Action
                                IconButton(onClick = { shareScreenshot(context, screenshot) }) {
                                    Icon(
                                        imageVector = Icons.Rounded.Share,
                                        contentDescription = "Share",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Delete Action
                                IconButton(
                                    onClick = {
                                        screenshotToDelete = screenshot
                                        showDeleteConfirm = true
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.Delete,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }

                                // Custom Collection Assignment Dialog
                                if (showCollectionDialog) {
                                    var allCollections by remember { mutableStateOf<List<String>>(emptyList()) }
                                    var newColName by remember { mutableStateOf("") }
                                    val activeCollections by viewModel.getCollectionsForScreenshot(screenshot.id)
                                        .collectAsState(initial = emptyList())

                                    LaunchedEffect(showCollectionDialog) {
                                        allCollections = viewModel.getCustomCollectionNames()
                                    }

                                    AlertDialog(
                                        onDismissRequest = { showCollectionDialog = false },
                                        title = { Text("Add to Custom Collection") },
                                        text = {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .heightIn(max = 300.dp),
                                                verticalArrangement = Arrangement.spacedBy(12.dp)
                                            ) {
                                                // Create collection field
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    OutlinedTextField(
                                                        value = newColName,
                                                        onValueChange = { newColName = it },
                                                        placeholder = { Text("New collection name") },
                                                        singleLine = true,
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                    Spacer(modifier = Modifier.width(8.dp))
                                                    IconButton(
                                                        onClick = {
                                                            if (newColName.isNotBlank()) {
                                                                val cleanName = newColName.trim()
                                                                viewModel.createCustomCollection(cleanName)
                                                                newColName = ""
                                                                // Re-fetch lists
                                                                androidx.compose.runtime.snapshots.Snapshot.withoutReadObservation {
                                                                    allCollections = (allCollections + cleanName).sorted()
                                                                }
                                                            }
                                                        }
                                                    ) {
                                                        Icon(Icons.Rounded.Add, contentDescription = "Add")
                                                    }
                                                }

                                                HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))

                                                // List of existing collections
                                                if (allCollections.isEmpty()) {
                                                    Text(
                                                        text = "No custom collections yet.",
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                        fontSize = 14.sp
                                                    )
                                                } else {
                                                    androidx.compose.foundation.lazy.LazyColumn(
                                                        verticalArrangement = Arrangement.spacedBy(8.dp),
                                                        modifier = Modifier.fillMaxWidth()
                                                    ) {
                                                        items(allCollections.size) { index ->
                                                            val name = allCollections[index]
                                                            val isChecked = activeCollections.contains(name)
                                                            Row(
                                                                modifier = Modifier
                                                                    .fillMaxWidth()
                                                                    .clickable {
                                                                        if (isChecked) {
                                                                            viewModel.removeScreenshotFromCustomCollection(screenshot.id, name)
                                                                        } else {
                                                                            viewModel.addScreenshotToCustomCollection(screenshot.id, name)
                                                                        }
                                                                    }
                                                                    .padding(vertical = 4.dp),
                                                                verticalAlignment = Alignment.CenterVertically
                                                            ) {
                                                                Checkbox(
                                                                    checked = isChecked,
                                                                    onCheckedChange = {
                                                                        if (isChecked) {
                                                                            viewModel.removeScreenshotFromCustomCollection(screenshot.id, name)
                                                                        } else {
                                                                            viewModel.addScreenshotToCustomCollection(screenshot.id, name)
                                                                        }
                                                                    }
                                                                )
                                                                Spacer(modifier = Modifier.width(8.dp))
                                                                Text(text = name, fontSize = 16.sp)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        confirmButton = {
                                            TextButton(onClick = { showCollectionDialog = false }) {
                                                Text("Done")
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Confirmation dialog before calling system delete request
    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Screenshot") },
            text = { Text("This will delete the actual image file from your device storage. Do you want to proceed?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        screenshotToDelete?.let {
                            viewModel.requestDelete(context, it)
                        }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun ZoomableImage(
    uri: String,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    val state = rememberTransformableState { zoomChange, panChange, _ ->
        scale = (scale * zoomChange).coerceIn(1f, 5f)
        if (scale > 1f) {
            offset += panChange
        } else {
            offset = Offset.Zero
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 3f
                        }
                    }
                )
            }
            .clipToBounds()
    ) {
        AsyncImage(
            model = uri,
            contentDescription = contentDescription,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .transformable(state = state, enabled = scale > 1f)
        )
    }
}

private fun formatEpochTime(epochMillis: Long): String {
    val date = Date(epochMillis)
    val format = SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.getDefault())
    return format.format(date)
}

private fun formatSize(bytes: Long): String {
    val kb = bytes / 1024.0
    val mb = kb / 1024.0
    return if (mb > 1.0) {
        String.format(Locale.US, "%.2f MB", mb)
    } else {
        String.format(Locale.US, "%.1f KB", kb)
    }
}

private fun shareScreenshot(context: Context, screenshot: Screenshot) {
    try {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_STREAM, Uri.parse(screenshot.imageUri))
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(Intent.createChooser(intent, "Share Screenshot"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun openInGallery(context: Context, screenshot: Screenshot) {
    try {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(Uri.parse(screenshot.imageUri), "image/*")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
