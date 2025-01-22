package com.ssafy.keywe.common.order

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuCategory(onClick: () -> Unit, selected: Boolean, category: String) {
    Button(
        onClick = { onClick() },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = whiteBackgroundColor // 버튼 내부 배경색
        ),
        shape = RoundedCornerShape(size = 10.dp),
        border = BorderStroke(
            width = 2.dp,
            color = if (selected) orangeColor else whiteBackgroundColor
        )
    ) {
        Text(
            text = category,
            color = titleTextColor,
            style = h6.copy(fontSize = 16.sp)
        )
    }
}

@Composable
fun MenuCategoryScreen(category: String) {
    // 선택 상태를 관리하는 상태 변수
    var isSelected by remember { mutableStateOf(false) }

    Column() {
        MenuCategory(
            onClick = {
                // 클릭 시 상태 변경
                isSelected = !isSelected
            },
            selected = isSelected,
            category = category
        )
    }


}