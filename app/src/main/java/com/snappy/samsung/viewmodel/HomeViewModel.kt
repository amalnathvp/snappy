package com.snappy.samsung.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.snappy.samsung.repository.CollectionInfo
import com.snappy.samsung.repository.ScreenshotRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: ScreenshotRepository) : ViewModel() {

    val collectionsState: StateFlow<HomeUiState> = combine(
        repository.getCollections(),
        repository.getCustomCollections(),
        repository.getFavoritesCountFlow(),
        repository.getLatestFavoriteUriFlow()
    ) { collections, customCollections, favoritesCount, latestFavoriteUri ->
        val favoritesCollection = if (favoritesCount > 0) {
            CollectionInfo(
                appName = "Favorites",
                count = favoritesCount,
                latestUri = latestFavoriteUri
            )
        } else null
        
        HomeUiState.Success(
            favorites = favoritesCollection,
            appCollections = collections,
            customCollections = customCollections
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
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val favorites: CollectionInfo?,
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
