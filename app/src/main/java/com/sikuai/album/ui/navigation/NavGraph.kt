package com.sikuai.album.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sikuai.album.ui.confirm.ConfirmScreen
import com.sikuai.album.ui.guide.GuideScreen
import com.sikuai.album.ui.home.HomeScreen
import com.sikuai.album.ui.my.MyScreen
import com.sikuai.album.ui.preview.PreviewScreen

object Routes {
    const val HOME = "home"
    const val MY = "my"
    const val PREVIEW = "preview"
    const val CONFIRM = "confirm"
    const val GUIDE = "guide"
}

@Composable
fun AppNavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.GUIDE) {
            GuideScreen(onGuideFinished = {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.GUIDE) { inclusive = true }
                }
            })
        }
        composable(Routes.HOME) { HomeScreen(navController) }
        composable(Routes.MY) { MyScreen(navController) }
        composable(Routes.PREVIEW) { PreviewScreen(navController) }
        composable(Routes.CONFIRM) { ConfirmScreen(navController) }
        composable(Routes.GUIDE) { GuideScreen(navController) }
    }
}
