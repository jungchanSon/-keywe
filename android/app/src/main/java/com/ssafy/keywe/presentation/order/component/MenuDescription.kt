package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.pretendardkr
import com.ssafy.keywe.ui.theme.titleTextColor

@Composable
fun MenuDescription(name: String, recipe: String, price: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            Text(
                text = name, style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Bold,
                    color = titleTextColor
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = recipe, style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Normal,
                    color = polishedSteelColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${price}원", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Bold,
                    color = titleTextColor
                )
            )
        }
        MenuPlusButton()
    }

}
