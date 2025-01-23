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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            .height(100.dp)
            .background(color = whiteBackgroundColor),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun MenuDetails(name: String, price: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = whiteBackgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier,
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
    Box(
        modifier = Modifier
//            .height(172.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(whiteBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuImage()
            MenuDetails(name, price)
        }
    }

}

@Composable
fun MenuSix(menuList: List<Pair<String, Int>>) {
    Box(
        modifier = Modifier
            .background(whiteBackgroundColor)
            .fillMaxSize()
            .padding(24.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
//                .padding(24.dp),
//            verticalArrangement = Arrangement.spacedBy(12.dp, CenterVertically),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            items(menuList.chunked(2)) { rowItems ->
                    Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowItems.forEach { menu ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        ) {
                            Menu(name = menu.first, price = menu.second)
                        }
                    }
                    if (rowItems.size < 2) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MenuCategoryScreen() {
    val menuList = listOf(
        "아메리카노" to 2000,
        "카페라떼" to 3000,
        "바닐라라떼" to 2500,
        "카페모카" to 4000,
        "콜드브루" to 4500
    )
    MenuSix(menuList = menuList)
}