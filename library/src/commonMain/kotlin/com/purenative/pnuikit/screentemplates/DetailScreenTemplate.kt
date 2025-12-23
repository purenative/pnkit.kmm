package com.purenative.pnuikit.screentemplates

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <Model> DetailScreenTemplate(
    modifier: Modifier = Modifier,

    isScrollable: Boolean,
    padding: PaddingValues = PaddingValues(16.dp),

    isLoading: Boolean,
    model: Model?,
    error: String = "",

    detailBuilder: @Composable (Model) -> Unit,
    shimmerBuilder: @Composable () -> Unit = { },
    errorBuilder: @Composable (String) -> Unit = { }
) {
    if (error.isEmpty()) {
        if (isLoading) {
            Box(modifier) {
                shimmerBuilder()
            }
        } else {
            if (isScrollable) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(padding)
                ) {
                    model?.let {
                        detailBuilder(it)
                    }
                }
            } else {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    model?.let {
                        detailBuilder(it)
                    }
                }
            }
        }
    } else {
        Box(modifier) {
            errorBuilder(error)
        }
    }
}