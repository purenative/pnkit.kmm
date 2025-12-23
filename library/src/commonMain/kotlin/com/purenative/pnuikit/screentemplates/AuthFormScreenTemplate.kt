package com.purenative.pnuikit.screentemplates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun <Field> AuthFormScreenTemplate(
    fields: List<Field>,
    spacing: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    headerContent: @Composable () -> Unit,
    fieldContent: @Composable (Field) -> Unit,
    footerContent: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(padding),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Spacer(modifier = Modifier.weight(1f))

        headerContent()

        for (field in fields) {
            fieldContent(field)
        }

        footerContent()

        Spacer(modifier = Modifier.weight(1f))
    }
}