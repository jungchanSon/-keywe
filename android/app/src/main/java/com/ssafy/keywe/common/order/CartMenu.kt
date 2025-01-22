package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.body2
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import java.time.temporal.TemporalAmount

@Composable
fun CartMenuImage() {
    val imageUrl = "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"

    Box(
        modifier = Modifier
            .width(88.dp)
            .height(111.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = imageUrl),
            contentDescription = "Cart Menu Image",
            contentScale = ContentScale.Fit, // 이미지를 박스에 꽉 채움
            modifier = Modifier.fillMaxWidth() // 부모 크기에 맞춤
        )
    }
}

@Composable
fun CartMenuDescription(name: String, option: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center // 세로 중앙 정렬
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = name,
                style = h6sb
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = option,
                style = body2.copy(color = titleTextColor.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
fun CartAmountPrice(amount: Int, price: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        OptionAmount(amount)
        Text(
            text = "${price}원",
            style = h6
        )
    }
}

@Composable
fun CartMenu(name: String, option: String, amount: Int, price: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 24.dp)
            .background(whiteBackgroundColor),

        ){

        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .align(Alignment.TopStart)
                .zIndex(1f),
            painter = painterResource(R.drawable.checkbox),
            contentDescription = "checkbox"
        )
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .align(Alignment.TopEnd)
                .zIndex(1f),
            painter = painterResource(R.drawable.x_circle),
            contentDescription = "x in circle"
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(111.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CartMenuImage()
                    CartMenuDescription("아메리카노", "ICE, 얼음 많이")
                }
            }
            CartAmountPrice(1, 2000)
        }
    }
}