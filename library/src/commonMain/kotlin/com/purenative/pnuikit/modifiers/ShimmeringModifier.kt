package com.purenative.pnuikit.modifiers

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.withSaveLayer
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement

@Composable
fun Modifier.shimmering(
    fromColor: Color,
    toColor: Color,
    durationMills: Int = 750
): Modifier {
    val transition = rememberInfiniteTransition()
    val animatedColor by transition.animateColor(
        fromColor,
        toColor,
        infiniteRepeatable(
            tween(
                750,
                easing = EaseInOut
            ),
            RepeatMode.Reverse
        )
    )
    val effect = ShimmerEffect(animatedColor)
    return this then ShimmerElement(effect)
}

private data class ShimmerElement(
    var effect: ShimmerEffect
): ModifierNodeElement<ShimmerNode>() {
    override fun create(): ShimmerNode = ShimmerNode(effect)

    override fun update(node: ShimmerNode) {
        node.effect = effect
    }
}

private class ShimmerNode(
    var effect: ShimmerEffect
): Modifier.Node(), DrawModifierNode {
    override fun ContentDrawScope.draw() {
        with(effect) { draw() }
    }
}

internal class ShimmerEffect(
    val color: Color
) {
    private val paint = Paint().apply {
        isAntiAlias = true
        style = PaintingStyle.Fill
        blendMode = BlendMode.SrcAtop
        color = this@ShimmerEffect.color
    }

    private val emptyPaint = Paint()

    fun ContentDrawScope.draw() {
        val targetBounds = size.toRect()
        drawIntoCanvas { canvas ->
            canvas.withSaveLayer(targetBounds, emptyPaint) {
                drawContent()
                canvas.drawRect(targetBounds, paint)
            }
        }
    }
}