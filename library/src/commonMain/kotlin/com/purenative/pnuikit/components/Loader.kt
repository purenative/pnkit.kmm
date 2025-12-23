package com.purenative.pnuikit.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import kotlin.math.min

@Composable
fun Loader(
    modifier: Modifier = Modifier,
    borderWidth: Dp,
    foregroundColor: Color,
    backgroundColor: Color
) {
    val infiniteTransition = rememberInfiniteTransition()

    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                1000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    Box(
        modifier = modifier
            .padding(borderWidth / 2)
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val diameter = min(size.width, size.height)
            val arcSize = Size(diameter, diameter)

            if (backgroundColor != Color.Transparent) {
                drawArc(
                    color = backgroundColor,
                    startAngle = 0f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(borderWidth.toPx(), cap = StrokeCap.Round),
                    size = arcSize
                )
            }

            drawArc(
                color = foregroundColor,
                startAngle = 360f * progress,
                sweepAngle = 216f,
                useCenter = false,
                style = Stroke(borderWidth.toPx(), cap = StrokeCap.Round),
                size = arcSize
            )
        }
    }
}