package com.ssafy.keywe.presentation.kiosk.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.formFillColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.titleTextColor

@Composable
fun PhoneNumberDisplay(phoneNumber1: String, phoneNumber2: String, phoneNumber3: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PhoneBox(phoneNumber1, 64)
        Separator()
        PhoneBox(phoneNumber2, 92)
        Separator()
        PhoneBox(phoneNumber3, 92)
    }
}

@Composable
fun PhoneBox(number: String, width: Int) {
    Box(
        modifier = Modifier
            .size(width.dp, 62.dp)
            .background(color = formFillColor, shape = RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = number, style = h6)
    }
}

@Composable
fun Separator() {
    Box(
        modifier = Modifier.size(8.dp, 1.dp).background(color = titleTextColor)
    )
}