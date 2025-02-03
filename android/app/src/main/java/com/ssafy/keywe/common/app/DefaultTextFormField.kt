package com.ssafy.keywe.common.app

import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.LaunchedEffect
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
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    var isFocused by remember { mutableStateOf(false) }

    // 포커스 상태 감지
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction: Interaction ->
            when (interaction) {
                is FocusInteraction.Focus -> isFocused = true
                is FocusInteraction.Unfocus -> isFocused = false
            }
        }
    }

    Column {
        if (label.isNotEmpty()) {
            Text(text = label, style = button, modifier = modifier.padding(bottom = 8.dp))
        }
        OutlinedTextField(interactionSource = interactionSource,
            modifier = modifier,
            placeholder = {
                Text(
                    text = placeholder, style = body2.copy(color = polishedSteelColor)
                )
            },
            isError = isError,
            keyboardActions = KeyboardActions.Default,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = greyBackgroundColor,
                unfocusedContainerColor = greyBackgroundColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                errorTextColor = Color.Red,
                errorIndicatorColor = Color.Red,
                errorContainerColor = greyBackgroundColor
            ),
            keyboardOptions = keyboardOptions,
            singleLine = true,
            value = text,
            onValueChange = { newInput ->
                onValueChange(newInput) // 일반 텍스트의 경우 그대로 전달
            },
            visualTransformation = if (isPassword) {
                if (isPasswordVisible) {
                    VisualTransformation.None
                } else PasswordVisualTransformation()
            } else VisualTransformation.None,
            textStyle = body2,
            trailingIcon = {

                if (isPassword && isFocused) {
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
            })
    }
}
