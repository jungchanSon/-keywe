package com.ssafy.keywe.presentation.kiosk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.common.ext.fieldModifier
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h5
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun KeypadScreen(
    onNumberClick: (String) -> Unit,
    onBackspaceClick: () -> Unit,
    onConfirmClick: () -> Unit,
    onBackClick: () -> Unit,
    isConfirmEnabled: Boolean
) {
    val buttons = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("", "0", "Backspace")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .padding(bottom = 5.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.spacedBy(19.dp)
            ) {
                row.forEach { text ->
                    when (text) {
                        "" -> Spacer(Modifier.weight(1f))
                        "Backspace" -> BackspaceButton(Modifier.weight(1f), onBackspaceClick)
                        else -> KeypadButton(text, Modifier.weight(1f)) {
                            onNumberClick(text)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ActionButton("뒤로가기", Modifier, greyBackgroundColor, titleTextColor, true, onBackClick)
            ActionButton("확인", Modifier, primaryColor, whiteBackgroundColor, isConfirmEnabled, onConfirmClick)
        }
    }
}

@Composable
fun KeypadButton(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(88.dp).height(88.dp)
            .background(greyBackgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, style = h5)
    }
}

@Composable
fun BackspaceButton(modifier: Modifier, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .width(88.dp).height(88.dp)
            .background(greyBackgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Backspace,
            contentDescription = "Backspace",
            tint = titleTextColor
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    modifier: Modifier,
    backgroundColor: Color,
    textColor: Color,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .width(150.dp)
            .height(52.dp)
            .background(
                color = if (isEnabled) backgroundColor else greyBackgroundColor,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(enabled = isEnabled) { onClick()   },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = subtitle2.copy(fontWeight = FontWeight.Bold, letterSpacing = 0.em),
            color = if (isEnabled) textColor else polishedSteelColor
        )
    }
}
