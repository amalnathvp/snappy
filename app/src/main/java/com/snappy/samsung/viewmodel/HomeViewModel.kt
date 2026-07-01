package com.snappy.samsung.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snappy.samsung.repository.CollectionInfo
import com.snappy.samsung.repository.ScreenshotRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ScreenshotRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortMode = MutableStateFlow(HomeSortMode.BY_DATE)
    val sortMode = _sortMode.asStateFlow()

    private val favoritesFlow: Flow<CollectionInfo?> = combine(
        repository.getFavoritesCountFlow(),
        repository.getLatestFavoriteUriFlow()
    ) { count, latestUri ->
        if (count > 0) {
            CollectionInfo(
                appName = "Favorites",
                count = count,
                latestUri = latestUri
            )
        } else null
    }

    private val allScreenshotsFlow: Flow<CollectionInfo?> = combine(
        repository.getTotalCountFlow(),
        repository.getLatestScreenshotUriFlow()
    ) { count, latestUri ->
        if (count > 0) {
            CollectionInfo(
                appName = "All Screenshots",
                count = count,
                latestUri = latestUri
            )
        } else null
    }

    private data class RawHomeCollections(
        val appCollections: List<CollectionInfo>,
        val customCollections: List<CollectionInfo>,
        val favorites: CollectionInfo?,
        val allScreenshots: CollectionInfo?
    )

    private val rawCollectionsFlow: Flow<RawHomeCollections> = combine(
        repository.getCollections(),
        repository.getCustomCollections(),
        favoritesFlow,
        allScreenshotsFlow
    ) { collections, customCollections, favoritesCollection, allCollection ->
        RawHomeCollections(collections, customCollections, favoritesCollection, allCollection)
    }

    val collectionsState: StateFlow<HomeUiState> = combine(
        rawCollectionsFlow,
        _searchQuery,
        _sortMode
    ) { raw, query, sort ->
        var filteredApps = raw.appCollections
        if (query.isNotEmpty()) {
            filteredApps = filteredApps.filter { it.appName.contains(query, ignoreCase = true) }
        }
        filteredApps = when (sort) {
            HomeSortMode.BY_DATE -> filteredApps.sortedByDescending { it.latestDate }
            HomeSortMode.BY_COUNT -> filteredApps.sortedByDescending { it.count }
            HomeSortMode.BY_NAME -> filteredApps.sortedBy { it.appName }
        }

        var filteredCustoms = raw.customCollections
        if (query.isNotEmpty()) {
            filteredCustoms = filteredCustoms.filter { it.appName.contains(query, ignoreCase = true) }
        }
        filteredCustoms = when (sort) {
            HomeSortMode.BY_DATE -> filteredCustoms.sortedByDescending { it.latestDate }
            HomeSortMode.BY_COUNT -> filteredCustoms.sortedByDescending { it.count }
            HomeSortMode.BY_NAME -> filteredCustoms.sortedBy { it.appName }
        }

        HomeUiState.Success(
            favorites = raw.favorites,
            allScreenshots = raw.allScreenshots,
            appCollections = filteredApps,
            customCollections = filteredCustoms
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState.Loading
    )

    init {
        scanScreenshots()
    }

    fun scanScreenshots() {
        viewModelScope.launch {
            repository.syncScreenshots()
        }
    }

    fun createCustomCollection(name: String) {
        viewModelScope.launch {
            repository.createCustomCollection(name)
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setSortMode(mode: HomeSortMode) {
        _sortMode.value = mode
    }
}

enum class HomeSortMode {
    BY_DATE,
    BY_COUNT,
    BY_NAME
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val favorites: CollectionInfo?,
        val allScreenshots: CollectionInfo?,
        val appCollections: List<CollectionInfo>,
        val customCollections: List<CollectionInfo>
    ) : HomeUiState
}

class HomeViewModelFactory(private val repository: ScreenshotRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
