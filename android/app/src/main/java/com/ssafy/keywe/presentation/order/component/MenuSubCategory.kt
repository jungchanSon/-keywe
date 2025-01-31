package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.ssafy.keywe.ui.theme.h6

@Composable
fun MenuSubCategory(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 22.dp)
    ) {
        Text(
            text = text,
            style = h6.copy(fontWeight = FontWeight.Normal, letterSpacing = 0.em)
        )
    }
}