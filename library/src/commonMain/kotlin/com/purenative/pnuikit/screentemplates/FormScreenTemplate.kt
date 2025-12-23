package com.purenative.pnuikit.screentemplates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class FormScreenAcceptButtonConfiguration(
    val title: String,
    val isEnabled: Boolean,
    val onTapped: () -> Unit
)

data class FormScreenFieldConfiguration<Field>(
    val field: Field,
    val modifier: Modifier
)

private class FormScreenTemplateState<Field> internal constructor(
    private val focusManager: FocusManager,
    private val fields: List<Field>,
    private val nextButtonTitle: String,
    private val acceptButtonTitle: String,
    private val onAcceptButtonTapped: () -> Unit
) {
    var focusedField by mutableStateOf<Field?>(null)
        private set

    private val focusRequesters = fields.associateBy({ it }, { FocusRequester() })

    val actionButtonTitle: String
        get() = if (fields.lastOrNull() == focusedField || focusedField == null) acceptButtonTitle else nextButtonTitle

    fun focusRequester(field: Field): FocusRequester = focusRequesters.getValue(field)

    fun setFocusedField(field: Field?, isFocused: Boolean) {
        field?.let {
            focusedField = if (isFocused) field else null
        } ?: run {
            focusedField = null
        }
    }

    fun setNextFocusedFieldAfter() {
        focusedField?.let {
            val nextFieldIndex = fields.indexOf(it) + 1
            if (nextFieldIndex < fields.count()) {
                val nextField = fields[nextFieldIndex]
                focusRequesters[nextField]?.requestFocus()
            } else {
                focusManager.clearFocus()
                onAcceptButtonTapped()
            }
        } ?: run {
            focusManager.clearFocus()
            onAcceptButtonTapped()
        }
    }
}

@Composable
private fun <Field> rememberFormScreenTemplateState(
    fields: List<Field>,
    nextButtonTitle: String,
    acceptButtonTitle: String,
    onAcceptButtonTapped: () -> Unit
): FormScreenTemplateState<Field> {
    val focusManager = LocalFocusManager.current
    var state = remember {
        FormScreenTemplateState(
            focusManager,
            fields,
            nextButtonTitle,
            acceptButtonTitle,
            onAcceptButtonTapped
        )
    }
    return state
}

@Composable
fun <Field> FormScreenTemplate(
    modifier: Modifier = Modifier,
    isScrollingEnabled: Boolean = true,
    spacing: Dp = 16.dp,
    padding: PaddingValues = PaddingValues(16.dp),
    fields: List<Field>,
    headerContent: @Composable () -> Unit = { },
    fieldContent: @Composable (FormScreenFieldConfiguration<Field>) -> Unit,
    footerContent: @Composable () -> Unit = { },
    nextButtonTitle: String,
    acceptButtonTitle: String,
    acceptButtonEnabled: Boolean,
    onAcceptButtonTapped: () -> Unit,
    acceptButtonContent: @Composable (FormScreenAcceptButtonConfiguration) -> Unit
) {
    val scrollState = rememberScrollState()
    val state = rememberFormScreenTemplateState(fields, nextButtonTitle, acceptButtonTitle, onAcceptButtonTapped)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(
                    scrollState,
                    enabled = isScrollingEnabled
                )
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(spacing),
        ) {
            headerContent()

            for (field in fields) {
                val configuration = FormScreenFieldConfiguration(
                    field,
                    Modifier
                        .focusRequester(state.focusRequester(field))
                        .onFocusChanged({
                            state.setFocusedField(field, it.isFocused)
                        })
                )
                fieldContent(configuration)
            }

            footerContent()
        }

        val acceptButtonConfiguration = FormScreenAcceptButtonConfiguration(
            state.actionButtonTitle,
            state.focusedField != null || acceptButtonEnabled,
            state::setNextFocusedFieldAfter
        )
        acceptButtonContent(acceptButtonConfiguration)
    }
}