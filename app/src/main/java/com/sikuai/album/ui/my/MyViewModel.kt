package com.sikuai.album.ui.my

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SettingsState(
    val isDarkMode: Boolean = false,
    val useDynamicColor: Boolean = true,
    val groupSize: Int = 10,
    val hasSeenGuide: Boolean = false
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {

    private object PrefKeys {
        val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
        val USE_DYNAMIC_COLOR = booleanPreferencesKey("use_dynamic_color")
        val GROUP_SIZE = intPreferencesKey("group_size")
        val HAS_SEEN_GUIDE = booleanPreferencesKey("has_seen_guide")
    }

    val settingsState: StateFlow<SettingsState> = dataStore.data
        .map { preferences ->
            SettingsState(
                isDarkMode = preferences[PrefKeys.IS_DARK_MODE] ?: false,
                useDynamicColor = preferences[PrefKeys.USE_DYNAMIC_COLOR] ?: true,
                groupSize = preferences[PrefKeys.GROUP_SIZE] ?: 10,
                hasSeenGuide = preferences[PrefKeys.HAS_SEEN_GUIDE] ?: false
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SettingsState()
        )

    fun setHasSeenGuide(hasSeen: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PrefKeys.HAS_SEEN_GUIDE] = hasSeen
            }
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PrefKeys.IS_DARK_MODE] = isDark
            }
        }
    }

    fun setDynamicColor(useDynamic: Boolean) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PrefKeys.USE_DYNAMIC_COLOR] = useDynamic
            }
        }
    }

    fun setGroupSize(size: Int) {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                preferences[PrefKeys.GROUP_SIZE] = size
            }
        }
    }
}
