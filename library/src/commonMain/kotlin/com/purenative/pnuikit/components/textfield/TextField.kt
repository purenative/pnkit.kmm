package com.purenative.pnuikit.components.textfield

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun TextField(
    modifier: Modifier = Modifier,
    formatter: TextFieldFormatter? = null,
    isSecureTextEntry: Boolean = false,
    value: String,
    textStyle: TextStyle,
    colorBrush: Brush,
    singleLine: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
    decorationBox: @Composable (@Composable () -> Unit) -> Unit
) {
    val visualTransformation = if (isSecureTextEntry)
        PasswordVisualTransformation()
    else (formatter?.transformation ?: VisualTransformation.None)

    BasicTextField(
        modifier = modifier.fillMaxWidth(),
        visualTransformation = visualTransformation,
        value = value,
        onValueChange = {
            val newValue = formatter?.process(it) ?: it
            onValueChange(newValue)
        },
        textStyle = textStyle,
        cursorBrush = colorBrush,
        singleLine = singleLine,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        decorationBox = decorationBox
    )
}