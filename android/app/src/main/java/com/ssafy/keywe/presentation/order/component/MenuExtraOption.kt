package com.ssafy.keywe.presentation.order.component

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
import androidx.compose.runtime.mutableIntStateOf
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
fun MenuExtraOption(onPriceChange: (Int) -> Unit) {
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
            OptionBox("샷추가", 500, 1, onPriceChange)
            OptionBox("시럽 추가", 0, 1, onPriceChange)
        }
    }
}

@Composable
fun Option(name: String, optionPrice: Int) {
    Text(
        text = if (optionPrice == 0) name else "$name + ${optionPrice}원",
        style = subtitle1
    )
}

@Composable
fun OptionAmount(optionAmount: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
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
            text = "$optionAmount",
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
fun OptionBox(name: String, optionPrice:Int, initialAmount: Int, onPriceChange: (Int) -> Unit ) {

    val optionAmount = remember { mutableIntStateOf(initialAmount) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = whiteBackgroundColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Option(name, optionPrice)
            OptionAmount(optionAmount.intValue,
                onDecrease = {
                if (optionAmount.intValue > 1) {
                    optionAmount.intValue--
                    onPriceChange(-optionPrice)
                }
            }, onIncrease = {
                if (optionAmount.intValue < 10) {
                    optionAmount.intValue++
                    onPriceChange(+optionPrice)
                }
            })
        }
    }
}