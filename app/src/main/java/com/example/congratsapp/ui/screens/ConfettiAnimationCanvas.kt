package com.example.congratsapp.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.random.Random

data class ConfettiParticle(
    var position: Offset,
    var velocity: Offset,
    var color: Color,
    var size: Float,
    var rotation: Float = 0f,
    var alpha: Float = 1f
)

@Composable
fun ConfettiAnimationCanvas(
    modifier: Modifier = Modifier,
    confettiState: ConfettiState = remember { ConfettiState() }
) {
    val colors = listOf(Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta)

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(50)
            confettiState.addParticle(
                position = Offset(Random.nextFloat() * 1000f, 0f),
                color = colors.random(),
                size = Random.nextFloat() * 20f + 10f
            )
            confettiState.updateParticles()
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
    ) {
        confettiState.particles.forEach { particle ->
            rotate(degrees = particle.rotation, pivot = particle.position) {
                drawRect(
                    color = particle.color,
                    topLeft = Offset(particle.position.x - particle.size / 2, particle.position.y - particle.size / 2),
                    size = Size(particle.size, particle.size),
                    alpha = particle.alpha
                )
            }
        }
    }
}

class ConfettiState {
    val particles: SnapshotStateList<ConfettiParticle> = mutableStateListOf()

    fun addParticle(
        position: Offset,
        color: Color,
        size: Float
    ) {
        val randomXVelocity = Random.nextFloat() * 20f - 10f
        val randomYVelocity = Random.nextFloat() * -30f - 10f
        val randomRotation = Random.nextFloat() * 360f

        particles.add(
            ConfettiParticle(
                position = position,
                velocity = Offset(randomXVelocity, randomYVelocity),
                color = color,
                size = size,
                rotation = randomRotation
            )
        )
    }

    fun updateParticles() {
        particles.removeAll { it.alpha <= 0f }
        particles.forEach { particle ->
            particle.position += particle.velocity
            particle.velocity = Offset(particle.velocity.x, particle.velocity.y + 0.5f)
            particle.alpha -= 0.01f
        }
    }
}