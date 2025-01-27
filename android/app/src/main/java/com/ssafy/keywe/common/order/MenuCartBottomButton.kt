package com.ssafy.keywe.common.order

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuCartBottomOrderButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    BottomButton(
        content = content,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonColors(
            containerColor = primaryColor,
            contentColor = whiteBackgroundColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )
}