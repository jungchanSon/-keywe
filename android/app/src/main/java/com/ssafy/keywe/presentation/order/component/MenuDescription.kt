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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.pretendardkr
import com.ssafy.keywe.ui.theme.titleTextColor

@Composable
fun MenuDescription(
    menuId: Int,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val menu = viewModel.getMenuDataById(menuId)

    val menuName = menu?.name ?: ""
    val menuRecipe = menu?.recipe ?: ""
    val menuPrice = menu?.price ?: 0

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Column(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = menuName, style = TextStyle(
                    fontSize = 15.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Bold,
                    color = titleTextColor
                )
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = menuRecipe,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(
                    fontSize = 13.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Normal,
                    color = polishedSteelColor
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "${menuPrice}원", style = TextStyle(
                    fontSize = 16.sp,
                    fontFamily = pretendardkr,
                    fontWeight = FontWeight.Bold,
                    color = titleTextColor
                )
            )
        }
        MenuPlusButton({
            viewModel.addToCart(
                menuId, "Tall","Hot", emptyMap(), menuPrice
            )
        })
    }

}
