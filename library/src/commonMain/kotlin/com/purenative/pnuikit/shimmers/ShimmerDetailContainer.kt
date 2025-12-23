package com.purenative.pnuikit.shimmers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.purenative.pnuikit.modifiers.shimmering

@Composable
fun ShimmerDetailContainer(
    fromColor: Color,
    toColor: Color,
    padding: PaddingValues = PaddingValues(16.dp),
    detailContent: @Composable () -> Unit
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
        Box(modifier = Modifier.padding(padding)) {
            detailContent()
        }
    }
}