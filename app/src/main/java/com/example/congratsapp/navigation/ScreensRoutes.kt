package com.example.congratsapp.navigation

sealed class ScreensRoutes(val route: String) {
    object Main: ScreensRoutes("mainScreen")
    object AnimatedBox: ScreensRoutes("animatedBox")
    object ConfettiAnimation: ScreensRoutes("confettiAnimation")
    object Lottie: ScreensRoutes("Lottie")
}