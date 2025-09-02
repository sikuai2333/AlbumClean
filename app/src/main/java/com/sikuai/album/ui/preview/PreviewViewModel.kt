package com.sikuai.album.ui.preview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikuai.album.data.local.PhotoEntity
import com.sikuai.album.data.repo.PhotoRepository
import com.sikuai.album.domain.usecase.GroupPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PreviewUiState(
    val currentGroup: List<PhotoEntity> = emptyList(),
    val keptPhotos: Set<String> = emptySet(), // Store URIs for efficiency
    val deletedPhotos: Set<String> = emptySet(),
    val isLoading: Boolean = true
) {
    val processedPhotos: Set<String> = keptPhotos + deletedPhotos
}

@HiltViewModel
class PreviewViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val groupPhotosUseCase: GroupPhotosUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(PreviewUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadNextGroup()
    }

    private fun loadNextGroup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // In a real app, this would come from DataStore
            val groupSize = 10
            // This logic is simplified: it gets all photos and processes the first group.
            // A real app would need to track processed photos and fetch the next unprocessed group.
            val allPhotos = photoRepository.getAllPhotos().first()
            val groups = groupPhotosUseCase(allPhotos, groupSize)

            if (groups.isNotEmpty()) {
                _uiState.update {
                    it.copy(
                        currentGroup = groups.first(),
                        isLoading = false,
                        keptPhotos = emptySet(),
                        deletedPhotos = emptySet()
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false, currentGroup = emptyList()) }
            }
        }
    }

    fun onPhotoKept(photo: PhotoEntity) {
        _uiState.update {
            it.copy(keptPhotos = it.keptPhotos + photo.uri)
        }
    }

    fun onPhotoDeleted(photo: PhotoEntity) {
        _uiState.update {
            it.copy(deletedPhotos = it.deletedPhotos + photo.uri)
        }
    }
}
