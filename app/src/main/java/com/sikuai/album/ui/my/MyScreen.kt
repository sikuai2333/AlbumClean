package com.sikuai.album.ui.my

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyScreen(
    navController: NavController,
    viewModel: MyViewModel = hiltViewModel()
) {
    val settingsState by viewModel.settingsState.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("我的") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            item { UserProfilePlaceholder() }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            // --- Appearance Settings ---
            item { SectionTitle("外观") }
            item {
                SettingsSwitchItem(
                    title = "深色模式",
                    checked = settingsState.isDarkMode,
                    onCheckedChange = viewModel::setDarkMode
                )
            }
            item {
                SettingsSwitchItem(
                    title = "动态主题色",
                    subtitle = "跟随系统壁纸颜色 (Android 12+)",
                    checked = settingsState.useDynamicColor,
                    onCheckedChange = viewModel::setDynamicColor
                )
            }

            // --- Grouping Settings ---
            item { SectionTitle("整理") }
            item {
                SettingsSliderItem(
                    title = "单组数量",
                    value = settingsState.groupSize,
                    onValueChange = { viewModel.setGroupSize(it.roundToInt()) }
                )
            }

            // --- Other ---
            item { SectionTitle("其他") }
            item { SettingsClickableItem(title = "关于") { /* TODO */ } }
            item { SettingsClickableItem(title = "隐私政策") { /* TODO */ } }
            item { SettingsClickableItem(title = "SSO 登录") { /* TODO */ } }
        }
    }
}

@Composable
private fun UserProfilePlaceholder() {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "User Avatar",
                modifier = Modifier.height(48.dp).width(48.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("sikuai", style = MaterialTheme.typography.titleLarge)
                Text("2643927725#qq.com", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsSwitchItem(
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            subtitle?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingsSliderItem(
    title: String,
    value: Int,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Row {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.weight(1f))
            Text(value.toString(), style = MaterialTheme.typography.bodyLarge)
        }
        Slider(
            value = value.toFloat(),
            onValueChange = onValueChange,
            valueRange = 5f..50f,
            steps = 8 // (50-5)/5 = 9 steps
        )
    }
}

@Composable
private fun SettingsClickableItem(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ArrowForwardIos, contentDescription = null)
    }
}
