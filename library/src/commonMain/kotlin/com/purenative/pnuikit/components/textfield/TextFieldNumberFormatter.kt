package com.purenative.pnuikit.components.textfield

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class TextFieldNumberFormatter(
    val formatSymbol: Char = 'N',
    val format: String
): TextFieldFormatter {
    private val requiredSymbolsLength = format.count { it == formatSymbol }

    override val transformation: VisualTransformation = TextFieldNumberTransformation(formatSymbol, format, requiredSymbolsLength)

    override fun process(text: String): String {
        val trimmedText = text.filter { it.isDigit() }
        val fixedLengthText =
            if (trimmedText.length > requiredSymbolsLength) trimmedText.substring(0..<requiredSymbolsLength) else trimmedText
        return fixedLengthText
    }
}

class TextFieldNumberTransformation(
    val formatSymbol: Char,
    val format: String,
    val requiredSymbolsLength: Int
): VisualTransformation {
    private val translator = TextFieldNumberOffsetMapping(formatSymbol, format)

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length > requiredSymbolsLength) text.text.substring(0..<requiredSymbolsLength) else text.text
        val numbers = trimmed.filter { it.isDigit() }

        var result = ""
        var nextNumberIndex: Int? = if (numbers.isEmpty()) null else numbers.indices.first

        for (formatChar in format) {
            nextNumberIndex?.let { numberIndex ->
                if (formatChar == formatSymbol) {
                    result += numbers[numberIndex]
                    nextNumberIndex = if (numberIndex + 1 < numbers.count()) numberIndex + 1 else null
                } else {
                    result += formatChar
                }
            } ?: break
        }

        return TransformedText(AnnotatedString(result), translator)
    }
}

private class TextFieldNumberOffsetMapping(
    val formatSymbol: Char,
    val format: String
): OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        var specialSymbolsCount = 0
        var textSymbolIndex = 0

        for (symbol in format) {
            if (textSymbolIndex == offset)
                break

            if (symbol == formatSymbol) {
                textSymbolIndex += 1
            } else {
                specialSymbolsCount += 1
            }
        }

        val result = offset + specialSymbolsCount
        return result
    }

    override fun transformedToOriginal(offset: Int): Int {
        var specialSymbolsCount = 0
        var formatSymbolIndex = 0

        for (symbol in format) {
            if (formatSymbolIndex == offset)
                break

            if (symbol != formatSymbol) {
                specialSymbolsCount += 1
            }
            formatSymbolIndex += 1
        }

        return offset - specialSymbolsCount
    }
}