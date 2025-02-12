package com.ssafy.keywe.common.app

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun BottomButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier
        .height(52.dp)
        .fillMaxWidth()
        .semantics { contentDescription = "$content button" },
    colors: ButtonColors = ButtonColors(
        containerColor = primaryColor,
        contentColor = whiteBackgroundColor,
        disabledContentColor = polishedSteelColor,
        disabledContainerColor = greyBackgroundColor
    ),
) {
    TextButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        colors = colors,
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text = content, style = subtitle2.copy(fontWeight = FontWeight.Bold))
    }
}