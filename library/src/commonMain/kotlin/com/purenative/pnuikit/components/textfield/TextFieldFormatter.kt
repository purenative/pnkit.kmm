package com.purenative.pnuikit.components.textfield

import androidx.compose.ui.text.input.VisualTransformation

interface TextFieldFormatter {
    val transformation: VisualTransformation

    fun process(text: String): String
}