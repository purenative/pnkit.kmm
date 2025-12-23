package com.purenative.pnuikit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

fun LazyListState.reachedBottom(): Boolean {
    if (layoutInfo.visibleItemsInfo.isEmpty()) {
        return false;
    }

    val lastVisibleItem = layoutInfo.visibleItemsInfo.last()
    val viewportHeight = layoutInfo.viewportEndOffset + layoutInfo.viewportStartOffset

    val bottomReached = (lastVisibleItem.index + 1 == layoutInfo.totalItemsCount && lastVisibleItem.offset + lastVisibleItem.size <= viewportHeight)
    return bottomReached
}

@Composable
fun SmartColumn(
    modifier: Modifier = Modifier,
    state: LazyListState,
    isScrollingEnabled: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    onEndReached: () -> Unit = {},
    content: LazyListScope.() -> Unit,
) {
    val reachedBottom by remember {
        derivedStateOf {
            state.reachedBottom()
        }
    }

    LaunchedEffect(reachedBottom) {
        if (reachedBottom) {
            onEndReached()
        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement,
        state = state,
        userScrollEnabled = isScrollingEnabled,
        content = content
    )
}