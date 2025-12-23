package com.purenative.pnuikit.shimmers

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun ShimmerItem(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(Color.Black)
    )
}