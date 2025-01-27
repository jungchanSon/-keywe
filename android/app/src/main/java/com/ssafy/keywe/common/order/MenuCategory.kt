package com.ssafy.keywe.common.order

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun MenuCategoryScreen(){
    val menuCategorylist = listOf(
        "커피",
        "음료",
        "에이드",
        "차"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(horizontal = 24.dp)
    ){
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            items(menuCategorylist){ menuCategories ->
                MenuCategory(menuCategories)
            }
        }
    }

}

@Composable
fun MenuCategory(menuCategory:String){
    Box{
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .background(color = Color.Transparent)
                .drawBehind {
                    val extraPadding = 10.dp
                    drawLine(
                        color = primaryColor, // 테두리 색상
                        start = androidx.compose.ui.geometry.Offset(-extraPadding.toPx(), size.height),
                        end = androidx.compose.ui.geometry.Offset(size.width + extraPadding.toPx(), size.height),
                        strokeWidth = 2.dp.toPx() // 선 두께
                    )
                },
        ){
            Text(text = menuCategory, Modifier.background(Color.Transparent))
        }
    }
}