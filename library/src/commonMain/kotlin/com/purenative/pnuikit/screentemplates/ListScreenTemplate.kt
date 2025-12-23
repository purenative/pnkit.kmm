package com.purenative.pnuikit.screentemplates

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.purenative.pnuikit.components.SmartColumn

data class ListScreenTemplateOffset(
    val initialFirstVisibleItemIndex: Int = 0,
    val initialFirstVisibleItemScrollOffset: Int = 0
)

@Composable
fun <Model> ListScreenTemplate(
    modifier: Modifier = Modifier,
    isScrollingEnabled: Boolean = true,
    listState: LazyListState = rememberLazyListState(),

    spacing: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),

    isLoading: Boolean,
    models: List<Model>,
    error: String = "",

    isItemTappable: (Model) -> Boolean = { true },
    onItemTapped: (Model) -> Unit = { },
    onEndReached: () -> Unit = { },

    topContentBuilder: (@Composable () -> Unit)? = null,
    itemBuilder: @Composable (Model) -> Unit,
    separatorBuilder: (@Composable () -> Unit)? = null,
    shimmerBuilder: @Composable () -> Unit = { },
    loaderBuilder: @Composable () -> Unit = { },
    errorBuilder: @Composable (String) -> Unit = { },
    onOffsetChanged: (ListScreenTemplateOffset) -> Unit = { }
) {
    val offset by remember {
        derivedStateOf {
            ListScreenTemplateOffset(
                listState.firstVisibleItemIndex,
                listState.firstVisibleItemScrollOffset
            )
        }
    }
    LaunchedEffect(offset) {
        onOffsetChanged(offset)
    }

    Box(modifier = modifier) {
        if (error.isEmpty()) {
            val needShowShimmer = isLoading && models.isEmpty()
            if (needShowShimmer) {
                shimmerBuilder()
            } else {
                SmartColumn(
                    modifier = Modifier
                        .fillMaxWidth(),
                    state = listState,
                    isScrollingEnabled = isScrollingEnabled,
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    contentPadding = padding,
                    onEndReached = onEndReached
                ) {
                    topContentBuilder?.let {
                        item {
                            it()
                        }
                    }

                    items(items = models) { model ->
                        Box(
                            modifier = Modifier
                                .clickable(
                                    enabled = isItemTappable(model),
                                    onClick = { onItemTapped(model) }
                                )
                        ) {
                            itemBuilder(model)
                        }

                        separatorBuilder?.let {
                            if (models.last() != model) {
                                Box(modifier = Modifier.padding(top = spacing)) {
                                    it()
                                }
                            }
                        }
                    }

                    item {
                        Box(
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val needShowNextPageLoader = isLoading && models.isNotEmpty()
                            if (needShowNextPageLoader) {
                                loaderBuilder()
                            }
                        }
                    }
                }
            }
        } else {
            errorBuilder(error)
        }
    }
}