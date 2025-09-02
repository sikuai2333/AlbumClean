package com.sikuai.album.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikuai.album.data.local.PhotoEntity
import com.sikuai.album.data.repo.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val photos: List<PhotoEntity> = emptyList(),
    val isLoading: Boolean = false,
)

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val photoRepository: PhotoRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(HomeUiState())
        val uiState = _uiState.asStateFlow()

        init {
            // Observe photo changes from the local database
            viewModelScope.launch {
                photoRepository.getAllPhotos().collect { photos ->
                    _uiState.update { it.copy(photos = photos) }
                }
            }
        }

        fun syncWithMediaStore() {
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true) }
                try {
                    photoRepository.syncWithMediaStore()
                } finally {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
