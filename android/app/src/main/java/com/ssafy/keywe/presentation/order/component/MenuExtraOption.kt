package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@Composable
fun MenuExtraOption() {
    Box(
        modifier = Modifier
            .background(greyBackgroundColor),
    ){
        Column(
            modifier = Modifier
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text ="추가 선택", style = h6.copy(fontSize = 16.sp))
            OptionBox("샷추가 +500", 1)
            OptionBox("시럽 추가", 1)
        }
    }
}

@Composable
fun Option(name: String) {
    Text(
        text = name,
        style = subtitle1
    )
}

@Composable
fun OptionAmount(amount: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Row(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .clickable{ onDecrease() },
            painter = painterResource(R.drawable.minus_circle),
            contentDescription = "minus in circle"
        )
        Text(
            text = "${amount}",
            style = subtitle1
        )
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .clickable { onIncrease() },
            painter = painterResource(R.drawable.plus_circle),
            contentDescription = "plus in circle"
        )
    }
}

@Composable
fun OptionBox(name: String, initialAmount: Int) {
    val amount = remember { mutableStateOf(initialAmount) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .fillMaxHeight()
            .height(40.dp)
            .background(
                color = whiteBackgroundColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Option(name)
            OptionAmount(amount.value, onDecrease = {
                if (amount.value > 1) { // 1 이하로 내려가지 않도록 제한
                    amount.value--
                }
            }, onIncrease = {
                amount.value++
            })
        }
    }
}