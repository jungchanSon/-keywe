package com.ssafy.keywe.common.app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.button
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor

@Composable
fun DefaultTextFormField(
    label: String,
    placeholder: String,
    text: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    // 핸드폰 번호 포맷팅 로직
    fun String.clearFormatting(): String = this.filter { it.isDigit() }
    fun String.formatAsPhoneNumber(): String {
        val digits = this.clearFormatting()
        return when {
            digits.length <= 3 -> digits
            digits.length <= 7 -> "${digits.take(3)}-${digits.substring(3)}"
            else -> "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
        }
    }

    Column {
        if (label.isNotEmpty()) {
            Text(text = label, style = button)
        }
        OutlinedTextField(
            modifier = modifier,
            placeholder = {
                Text(
                    text = placeholder, style = body2.copy(color = polishedSteelColor)
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = greyBackgroundColor,
                unfocusedContainerColor = greyBackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            value = text,
            onValueChange = { newInput ->
                // 핸드폰 번호 포맷 적용
                val formattedInput = newInput.formatAsPhoneNumber()
                onValueChange(formattedInput) // 부모로 전달
            },
            visualTransformation = if (isPassword) {
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else PasswordVisualTransformation()
            } else VisualTransformation.None,
            textStyle = body2,
            trailingIcon = {
                if (!isPassword) null
                else {
                    if (isPasswordVisible) {
                        IconButton(onClick = { isPasswordVisible = false }) {
                            Icon(Icons.Filled.Visibility, contentDescription = "")
                        }
                    } else {
                        IconButton(onClick = { isPasswordVisible = true }) {
                            Icon(Icons.Filled.VisibilityOff, contentDescription = "")
                        }
                    }
                }
            }
        )
    }
}
