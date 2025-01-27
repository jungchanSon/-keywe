package com.ssafy.keywe.common.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailBottom() {
    Box(modifier = Modifier.background(greyBackgroundColor)) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 30.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomBackButton("담기", onClick = {})
            }
            Box(modifier = Modifier.weight(1f)) {
                MenuDetailBottomCartButton("주문하기", onClick = {})
            }
        }
    }
}

@Composable
fun MenuDetailBottomBackButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    BottomButton(
        content = content,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = whiteBackgroundColor,
            contentColor = titleTextColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )
}

@Composable
fun MenuDetailBottomCartButton(
    content: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    BottomButton(
        content = content,
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        colors = ButtonColors(
            containerColor = primaryColor,
            contentColor = whiteBackgroundColor,
            disabledContentColor = polishedSteelColor,
            disabledContainerColor = greyBackgroundColor
        )
    )
}