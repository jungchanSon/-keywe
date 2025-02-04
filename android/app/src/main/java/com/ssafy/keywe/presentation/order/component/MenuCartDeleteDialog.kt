package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.titleTextColor

@Composable
fun MenuCartDeleteDialog(
    title: String,
    description: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = { onCancel() }) {
        Surface(
            modifier = Modifier
                .width(280.dp) // 다이얼로그 크기 조절
                .clip(androidx.compose.foundation.shape.RoundedCornerShape(12.dp)) // 둥근 모서리 적용
                .background(Color.White),
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = title, style = h6sb)
                Text(text = description, style = subtitle1)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BottomButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        content = "뒤로가기",
                        onClick = onCancel,
                        colors = ButtonColors(
                            containerColor = greyBackgroundColor,
                            contentColor = titleTextColor,
                            disabledContentColor = polishedSteelColor,
                            disabledContainerColor = greyBackgroundColor
                        )
                    )
                    BottomButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        content = "확인",
                        onClick = onConfirm
                    )
                }
            }
        }
    }
}