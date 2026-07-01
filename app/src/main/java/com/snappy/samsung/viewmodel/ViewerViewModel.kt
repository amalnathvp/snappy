package com.snappy.samsung.viewmodel

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snappy.samsung.model.Screenshot
import com.snappy.samsung.repository.ScreenshotRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed interface ViewerUiState {
    data object Loading : ViewerUiState
    data class Success(
        val screenshots: List<Screenshot>,
        val initialIndex: Int
    ) : ViewerUiState
}

class ViewerViewModel(
    private val appName: String,
    private val initialId: Long,
    private val repository: ScreenshotRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ViewerUiState>(ViewerUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _deletePendingIntent = MutableSharedFlow<PendingIntent>()
    val deletePendingIntent = _deletePendingIntent.asSharedFlow()

    init {
        viewModelScope.launch {
            val flow = when (appName) {
                "Favorites" -> repository.getFavoriteScreenshots()
                "All" -> repository.getAllScreenshots()
                else -> repository.getScreenshotsByApp(appName)
            }
            flow.collectLatest { list ->
                val index = list.indexOfFirst { it.id == initialId }.coerceAtLeast(0)
                _uiState.value = ViewerUiState.Success(list, index)
            }
        }
    }

    fun toggleFavorite(screenshot: Screenshot) {
        viewModelScope.launch {
            repository.toggleFavorite(screenshot.id, !screenshot.favorite)
        }
    }

    fun requestDelete(context: Context, screenshot: Screenshot) {
        viewModelScope.launch {
            try {
                val uri = Uri.parse(screenshot.imageUri)
                val pendingIntent = MediaStore.createDeleteRequest(context.contentResolver, listOf(uri))
                _deletePendingIntent.emit(pendingIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onDeleteConfirmed(screenshotId: Long) {
        viewModelScope.launch {
            repository.deleteFromDatabase(screenshotId)
        }
    }

    fun getCollectionsForScreenshot(screenshotId: Long): Flow<List<String>> =
        repository.getCollectionsForScreenshotFlow(screenshotId)

    suspend fun getCustomCollectionNames(): List<String> =
        repository.getCustomCollectionNames()

    fun addScreenshotToCustomCollection(screenshotId: Long, collectionName: String) {
        viewModelScope.launch {
            repository.addScreenshotToCustomCollection(screenshotId, collectionName)
        }
    }

    fun removeScreenshotFromCustomCollection(screenshotId: Long, collectionName: String) {
        viewModelScope.launch {
            repository.removeScreenshotFromCustomCollection(screenshotId, collectionName)
        }
    }

    fun createCustomCollection(name: String) {
        viewModelScope.launch {
            repository.createCustomCollection(name)
        }
    }
}

class ViewerViewModelFactory(
    private val appName: String,
    private val initialId: Long,
    private val repository: ScreenshotRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewerViewModel(appName, initialId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
