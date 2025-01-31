package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.transformable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuMenuList(
    menuList: List<Quadruple<String, String, Int, String>>,
    navController: NavController,
) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(horizontal = 24.dp),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2), // 2열 그리드
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(menuList) { menu ->
                MenuMenuScreen(
                    menuName = menu.name,
                    menuRecipe = menu.recipe,
                    menuPrice = menu.price,
                    imageURL = menu.imageURL,
                    selectItem = {
                        navController.navigate("menuDetail")
                    }
                )
            }
        }
    }
}

@Composable
fun MenuMenuScreen(
    menuName: String,
    menuRecipe: String,
    menuPrice: Int,
    imageURL: String,
    selectItem: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth() // 부모의 너비를 전부 차지
            .wrapContentHeight() // 내용물에 맞게 높이 조절
            .clickable { selectItem() },
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(190.dp)
                .padding(bottom = 15.dp)
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MenuImage(imageURL) // 자동 크기 조정
            MenuDescription(menuName, menuRecipe, menuPrice) // 자동 크기 조정
        }
//        Card(
//            shape = RoundedCornerShape(16.dp),
//            elevation = 8.dp, // 그림자 깊이 조정
//            modifier = Modifier.wrapContentSize().rotate(40f) // 크기 조정
//        ){
//            Image(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(130.dp)
//                    .zIndex(-1f),
//                painter = painterResource(R.drawable.menu_background_rectangle),
//                contentDescription = "menu background rectangle",
//                contentScale = ContentScale.FillBounds
//            )
//        }
        val density = LocalDensity.current.density // Density 가져오기

        Box(
            modifier = Modifier
                .zIndex(-1f)
                .width(160.dp)
                .height(145.dp)
                .shadow(elevation = 10.dp, spotColor = Color(0x88696969), ambientColor = Color(0x11696969))
                .graphicsLayer(
                    rotationX = 15f, // X축 기준으로 30도 회전
                    cameraDistance = 8f * density // 원근감을 위해 카메라 거리 설정
                )
                .background(
                    whiteBackgroundColor,
                    shape = RoundedCornerShape(24.dp)
                )
        )

    }
}