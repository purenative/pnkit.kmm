package com.purenative.pnuikit.shimmers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.purenative.pnuikit.modifiers.shimmering

@Composable
fun ShimmerListContainer(
    fromColor: Color,
    toColor: Color,
    itemsCount: Int,
    spacing: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    topContent: @Composable () -> Unit = { },
    itemContent: @Composable () -> Unit,
    separatorContent: @Composable () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(
                rememberScrollState(),
                enabled = false
            )
            .shimmering(fromColor, toColor)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(spacing)
        ) {
            topContent()

            for (index in 0..<itemsCount) {
                itemContent()

                if (index < itemsCount - 1) {
                    separatorContent()
                }
            }
        }
    }
}