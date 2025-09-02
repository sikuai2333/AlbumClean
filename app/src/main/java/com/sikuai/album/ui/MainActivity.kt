package com.sikuai.album.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.sikuai.album.ui.my.MyViewModel
import com.sikuai.album.ui.navigation.AppNavGraph
import com.sikuai.album.ui.navigation.Routes
import com.sikuai.album.ui.theme.AlbumManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This enables edge-to-edge display
        enableEdgeToEdge()
        setContent {
            val settingsViewModel: MyViewModel = hiltViewModel()
            val settingsState by settingsViewModel.settingsState.collectAsState()

            // The AlbumManagerTheme will handle dynamic colors and dark mode settings from DataStore
            AlbumManagerTheme(
                darkTheme = settingsState.isDarkMode,
                dynamicColor = settingsState.useDynamicColor,
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    // AppNavGraph will host all the screens
                    val startDestination = if (settingsState.hasSeenGuide) Routes.HOME else Routes.GUIDE
                    AppNavGraph(startDestination = startDestination)
                }
            }
        }
    }
}
