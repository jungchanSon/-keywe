package com.ssafy.keywe.common.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
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
) {
    var isPasswordVisible by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = label, style = button)
        OutlinedTextField(placeholder = {
            Text(
                text = placeholder, style = body2.copy(color = polishedSteelColor)
            )
        },
            shape = RoundedCornerShape(8.dp),
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Number
//            ),
            colors = TextFieldDefaults.colors().copy(
                focusedContainerColor = greyBackgroundColor,
                unfocusedContainerColor = greyBackgroundColor,
                focusedIndicatorColor = Color.Transparent, // 포커스 시 경계선 색상 제거
                unfocusedIndicatorColor = Color.Transparent // 비포커스 시 경계선 색상 제거
            ),
            singleLine = true,
            value = text,
            onValueChange = onValueChange,
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
                        IconButton(onClick = {
                            isPasswordVisible = false
                        }) {
                            Icon(Icons.Filled.Visibility, contentDescription = "")
                        }
                    } else {
                        IconButton(onClick = {
                            isPasswordVisible = true
                        }) {
                            Icon(Icons.Filled.VisibilityOff, contentDescription = "")
                        }
                    }
                }

            })
    }
}
