package com.snappy.samsung.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snappy.samsung.model.Screenshot
import com.snappy.samsung.repository.ScreenshotRepository
import kotlinx.coroutines.flow.*

class CollectionViewModel(
    private val appName: String,
    private val repository: ScreenshotRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortMode = MutableStateFlow(SortMode.NEWEST_FIRST)
    val sortMode = _sortMode.asStateFlow()

    private val _favoritesOnly = MutableStateFlow(appName == "Favorites")
    val favoritesOnly = _favoritesOnly.asStateFlow()

    private val _selectedDate = MutableStateFlow<Long?>(null)
    val selectedDate = _selectedDate.asStateFlow()

    private val rawScreenshotsFlow: Flow<List<Screenshot>> = when (appName) {
        "Favorites" -> repository.getFavoriteScreenshots()
        "All" -> repository.getAllScreenshots()
        else -> flow {
            val customNames = repository.getCustomCollectionNames()
            if (customNames.contains(appName)) {
                emitAll(repository.getScreenshotsByCustomCollection(appName))
            } else {
                emitAll(repository.getScreenshotsByApp(appName))
            }
        }
    }

    val screenshotsState: StateFlow<List<Screenshot>> = combine(
        rawScreenshotsFlow,
        _searchQuery,
        _sortMode,
        _favoritesOnly,
        _selectedDate
    ) { list, query, sort, favorites, dateMillis ->
        var filteredList = list

        if (query.isNotEmpty()) {
            filteredList = filteredList.filter { screenshot ->
                screenshot.filename.contains(query, ignoreCase = true) ||
                        screenshot.appName.contains(query, ignoreCase = true)
            }
        }

        if (favorites && appName != "Favorites") {
            filteredList = filteredList.filter { it.favorite }
        }

        if (dateMillis != null) {
            val targetDateStr = formatDateToDayString(dateMillis)
            filteredList = filteredList.filter { screenshot ->
                formatDateToDayString(screenshot.dateTaken) == targetDateStr
            }
        }

        filteredList = when (sort) {
            SortMode.NEWEST_FIRST -> filteredList.sortedByDescending { it.dateTaken }
            SortMode.OLDEST_FIRST -> filteredList.sortedBy { it.dateTaken }
        }

        filteredList
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortMode(mode: SortMode) {
        _sortMode.value = mode
    }

    fun setFavoritesOnly(only: Boolean) {
        if (appName != "Favorites") {
            _favoritesOnly.value = only
        }
    }

    fun setSelectedDate(dateMillis: Long?) {
        _selectedDate.value = dateMillis
    }

    private fun formatDateToDayString(timeMillis: Long): String {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
        return sdf.format(java.util.Date(timeMillis))
    }
}

enum class SortMode {
    NEWEST_FIRST,
    OLDEST_FIRST
}

class CollectionViewModelFactory(
    private val appName: String,
    private val repository: ScreenshotRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CollectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CollectionViewModel(appName, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
