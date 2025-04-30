package com.example.congratsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.congratsapp.ui.screens.ComposableAnimations
import com.example.congratsapp.ui.screens.ConfettiAnimationCanvas
import com.example.congratsapp.ui.screens.Lottie
import com.example.congratsapp.ui.screens.MainScreen

@Composable
fun AppNavigation(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = ScreensRoutes.Main.route
    ) {
        composable(
            route = ScreensRoutes.Main.route
        ) {
            MainScreen(navController)
        }

        composable(
            route = ScreensRoutes.AnimatedBox.route,
        ) {
            ComposableAnimations()
        }

        composable(
            route = ScreensRoutes.ConfettiAnimation.route
        ) {
            ConfettiAnimationCanvas()
        }

        composable(
            route = ScreensRoutes.Lottie.route
        ) {
            Lottie()
        }
    }
}