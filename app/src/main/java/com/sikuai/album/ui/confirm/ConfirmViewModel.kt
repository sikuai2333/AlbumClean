package com.sikuai.album.ui.confirm

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sikuai.album.data.local.PhotoEntity
import com.sikuai.album.util.TmpDataHolder
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

data class ConfirmUiState(
    val keptPhotos: List<PhotoEntity> = emptyList(),
    val deletedPhotos: List<PhotoEntity> = emptyList(),
    val isDeleting: Boolean = false,
)

@HiltViewModel
class ConfirmViewModel
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ConfirmUiState())
        val uiState = _uiState.asStateFlow()

        init {
            _uiState.update {
                it.copy(
                    keptPhotos = TmpDataHolder.keptPhotos,
                    deletedPhotos = TmpDataHolder.deletedPhotos,
                )
            }
        }

        fun confirmDeletion(onDeletionComplete: () -> Unit) {
            viewModelScope.launch {
                _uiState.update { it.copy(isDeleting = true) }
                withContext(Dispatchers.IO) {
                    try {
                        val urisToDelete = _uiState.value.deletedPhotos.map { Uri.parse(it.uri) }
                        urisToDelete.forEach { uri ->
                            context.contentResolver.delete(uri, null, null)
                        }
                    } finally {
                        // This should run on the main thread if it updates UI state that triggers navigation
                        withContext(Dispatchers.Main) {
                            _uiState.update { it.copy(isDeleting = false) }
                            TmpDataHolder.keptPhotos = emptyList()
                            TmpDataHolder.deletedPhotos = emptyList()
                            onDeletionComplete()
                        }
                    }
                }
            }
        }
    }
