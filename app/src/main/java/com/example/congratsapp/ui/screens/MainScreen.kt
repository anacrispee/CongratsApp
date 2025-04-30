package com.example.congratsapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.congratsapp.R
import com.example.congratsapp.navigation.ScreensRoutes

@Composable
fun MainScreen(
    navController: NavHostController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier
                .padding(24.dp),
            text = stringResource(R.string.main_screen_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )

        Button(
            onClick = { navController.navigate(ScreensRoutes.ConfettiAnimation.route) }
        ) { Text(stringResource(R.string.confetti_animation_button)) }

        Button(
            onClick = { navController.navigate(ScreensRoutes.AnimatedBox.route) }
        ) { Text(stringResource(R.string.composable_animations_button)) }

        Button(
            onClick = { navController.navigate(ScreensRoutes.Lottie.route) }
        ) { Text(stringResource(R.string.lottie_button)) }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    MainScreen(
        navController = rememberNavController()
    )
}