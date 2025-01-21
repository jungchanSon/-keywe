package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.ui.theme.body1
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuImage() {

    val imageUrl = "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"

    Image(
        painter = rememberAsyncImagePainter(model = imageUrl),
        contentDescription = "Web Image",
        modifier = Modifier
            .fillMaxWidth()
            .background(color = whiteBackgroundColor),
        contentScale = ContentScale.Crop
    )

}

@Composable
fun MenuDetails(name: String, price: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = whiteBackgroundColor)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = name,
                color = primaryColor,
                modifier = Modifier.padding(bottom = 4.dp),
                style = h6sb
            )
            Text(
                text = "${price}원",
                color = titleTextColor,
                style = body1
            )
        }
    }
}

@Composable
fun Menu(name: String = "Menu", price: Int = 1000) {

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(whiteBackgroundColor),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MenuImage()
        MenuDetails(name, price)
    }

}