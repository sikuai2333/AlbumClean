package com.sikuai.album.ui.preview

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.sikuai.album.data.local.PhotoEntity
import com.sikuai.album.ui.navigation.Routes
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PreviewScreen(
    navController: NavController,
    viewModel: PreviewViewModel = hiltViewModel(),
) {
    val systemUiController = rememberSystemUiController()
    DisposableEffect(systemUiController) {
        systemUiController.isSystemBarsVisible = false
        onDispose {
            systemUiController.isSystemBarsVisible = true
        }
    }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isGroupComplete) {
        if (uiState.isGroupComplete) {
            viewModel.prepareForConfirmation()
            navController.navigate(Routes.CONFIRM) {
                popUpTo(Routes.PREVIEW) { inclusive = true }
            }
        }
    }

    if (uiState.isGroupComplete) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                CircularProgressIndicator()
                Text("处理完成...")
            }
        }
    } else {
        if (uiState.currentGroup.isEmpty()) {
            // Handle empty state or loading
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text("没有需要处理的照片。")
                }
            }
            return
        }

        val pagerState = rememberPagerState(pageCount = { uiState.currentGroup.size })
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { pageIndex ->
            val photo = uiState.currentGroup[pageIndex]
            // We still render the item box to occupy space, preventing the pager from collapsing
            Box(Modifier.fillMaxSize()) {
                if (photo.uri !in uiState.processedPhotos) {
                    SwipablePhoto(
                        photo = photo,
                        onSwipeUp = { viewModel.onPhotoKept(photo) },
                        onSwipeDown = { viewModel.onPhotoDeleted(photo) },
                    )
                }
            }
        }
    }
}

@Composable
private fun SwipablePhoto(
    photo: PhotoEntity,
    onSwipeUp: () -> Unit,
    onSwipeDown: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f) }
    var hasSwiped by remember { mutableStateOf(false) }

    Box(
        modifier =
            Modifier
                .fillMaxSize()
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragEnd = {
                            scope.launch {
                                // Animate back to center if not swiped far enough
                                if (offsetY.value > -200 && offsetY.value < 200) {
                                    offsetY.animateTo(0f, tween(300))
                                }
                            }
                        },
                    ) { change, dragAmount ->
                        change.consume()
                        scope.launch {
                            offsetY.snapTo(offsetY.value + dragAmount.y)
                            if (offsetY.value < -400 && !hasSwiped) { // Swipe Up (Keep)
                                hasSwiped = true
                                onSwipeUp()
                            } else if (offsetY.value > 400 && !hasSwiped) { // Swipe Down (Delete)
                                hasSwiped = true
                                onSwipeDown()
                            }
                        }
                    }
                },
    ) {
        AsyncImage(
            model =
                ImageRequest.Builder(LocalContext.current)
                    .data(photo.uri)
                    .crossfade(true)
                    .build(),
            contentDescription = "Full screen photo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
