package com.example.congratsapp.ui.screens

import androidx.compose.animation.animateColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.congratsapp.R

@Composable
fun ComposableAnimations() {
    var isBoxVisible by remember {
        mutableStateOf(true)
    }
    var animateBackgroundColor by remember {
        mutableStateOf(false)
    }
    var isBoxExpanded by remember {
        mutableStateOf(false)
    }

    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlpha by animateFloatAsState(
        targetValue = if (isBoxVisible) 1.0f else 0f,
        animationSpec = tween(durationMillis = 1000)
    )

    val animatedColor by infiniteTransition.animateColor(
        initialValue = if (animateBackgroundColor) Color.Blue else Color.Red,
        targetValue = if (animateBackgroundColor) Color.Green else Color.Gray,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing,
                delayMillis = 1000
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val animatedHeight by animateDpAsState(
        targetValue = if (isBoxExpanded) 100.dp else 50.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh
        )
    )

    val animatedWidth by animateDpAsState(
        targetValue = if (isBoxExpanded) 100.dp else 50.dp,
        animationSpec = spring(
            stiffness = Spring.StiffnessHigh
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer
            ),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(24.dp),
            text = stringResource(R.string.composable_animations_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )

        Box(
            modifier = Modifier
                .padding(bottom = 24.dp)
                .size(animatedHeight, animatedWidth)
                .graphicsLayer {
                    alpha = animatedAlpha
                }
                .clip(RoundedCornerShape(16.dp))
                .background(animatedColor)
                .animateContentSize(
                    animationSpec = tween(durationMillis = 1000)
                )
        )

        Button(
            onClick = { isBoxVisible = isBoxVisible.not() }
        ) { Text(stringResource(R.string.toggle_box_visibility)) }

        Button(
            onClick = { animateBackgroundColor = animateBackgroundColor.not() }
        ) { Text(stringResource(R.string.change_box_color)) }

        Button(
            onClick = { isBoxExpanded = isBoxExpanded.not() }
        ) { Text(stringResource(R.string.change_box_size)) }
    }
}

@Preview
@Composable
private fun ScreenPreview() {
    ComposableAnimations()
}