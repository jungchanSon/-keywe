package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun OptionName(name: String) {

}

@Composable
fun OptionPrice(price: Int) {

}

@Composable
fun OptionAmount(amount: Int) {
    Row(

    ) {
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp),
            painter = painterResource(R.drawable.minus_circle),
            contentDescription = ""
        )
        Text("${amount}")
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp),
            painter = painterResource(R.drawable.plus_circle),
            contentDescription = ""
        )
    }
}

@Composable
fun OptionBox(name: String, price: Int, amount: Int) {
    Box(
        modifier = Modifier
            .width(312.dp)
            .height(36.dp)
            .background(
                color = whiteBackgroundColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {

        }
    }
}