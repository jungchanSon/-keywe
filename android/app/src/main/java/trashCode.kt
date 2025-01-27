//package com.ssafy.keywe.common.order
//
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.compose.ui.zIndex
//import coil.compose.rememberAsyncImagePainter
//import com.ssafy.keywe.R
//import com.ssafy.keywe.ui.theme.body1
//import com.ssafy.keywe.ui.theme.body2
//import com.ssafy.keywe.ui.theme.greyBackgroundColor
//import com.ssafy.keywe.ui.theme.h6
//import com.ssafy.keywe.ui.theme.h6sb
//import com.ssafy.keywe.ui.theme.lightColor
//import com.ssafy.keywe.ui.theme.orangeColor
//import com.ssafy.keywe.ui.theme.primaryColor
//import com.ssafy.keywe.ui.theme.titleTextColor
//import com.ssafy.keywe.ui.theme.whiteBackgroundColor
//
//@Composable
//fun MenuImage() {
//    val imageUrl = "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
//
//    Image(
//        painter = rememberAsyncImagePainter(model = imageUrl),
//        contentDescription = "Web Image",
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(100.dp)
//            .background(color = whiteBackgroundColor),
//        contentScale = ContentScale.Fit
//    )
//}
//
//@Composable
//fun MenuDetails(name: String, price: Int) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .background(color = whiteBackgroundColor),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(
//            modifier = Modifier,
//            verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
//            horizontalAlignment = Alignment.CenterHorizontally,
//        ) {
//            Text(
//                text = name,
//                color = primaryColor,
//                modifier = Modifier.padding(bottom = 4.dp),
//                style = h6sb
//            )
//            Text(
//                text = "${price}원",
//                color = titleTextColor,
//                style = body1
//            )
//        }
//    }
//}
//
//@Composable
//fun Menu(name: String = "Menu", price: Int = 1000) {
//    Box(
//        modifier = Modifier
////            .height(172.dp),
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight()
//                .background(whiteBackgroundColor),
//            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            MenuImage()
//            MenuDetails(name, price)
//        }
//    }
//
//}
//
//@Composable
//fun MenuSix(menuList: List<Pair<String, Int>>) {
//    Box(
//        modifier = Modifier
//            .background(whiteBackgroundColor)
//            .fillMaxSize()
//            .padding(24.dp),
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxWidth()
//                .fillMaxHeight(),
////                .padding(24.dp),
////            verticalArrangement = Arrangement.spacedBy(12.dp, CenterVertically),
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            items(menuList.chunked(2)) { rowItems ->
//                Row(
//                    modifier = Modifier.fillMaxWidth().fillMaxHeight(),
//                    horizontalArrangement = Arrangement.spacedBy(20.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    rowItems.forEach { menu ->
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .fillMaxHeight()
//                        ) {
//                            Menu(name = menu.first, price = menu.second)
//                        }
//                    }
//                    if (rowItems.size < 2) {
//                        Box(
//                            modifier = Modifier
//                                .weight(1f)
//                                .fillMaxHeight()
//                        )
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun MenuCategoryScreen() {
//    val menuList = listOf(
//        "아메리카노" to 2000,
//        "카페라떼" to 3000,
//        "바닐라라떼" to 2500,
//        "카페모카" to 4000,
//        "콜드브루" to 4500
//    )
//    MenuSix(menuList = menuList)
//}
//
//@Composable
//fun MenuCategoryBlock(onClick: () -> Unit, selected: Boolean, category: String) {
//    Button(
//        onClick = { onClick() },
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//        colors = ButtonDefaults.outlinedButtonColors(
//            containerColor = whiteBackgroundColor // 버튼 내부 배경색
//        ),
//        shape = RoundedCornerShape(size = 10.dp),
//        border = BorderStroke(
//            width = 2.dp,
//            color = if (selected) orangeColor else whiteBackgroundColor
//        ),
//        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
//    ) {
//        Text(
//            text = category,
//            color = titleTextColor,
//            style = h6.copy(fontSize = 16.sp)
//        )
//    }
//}
//
//@Composable
//fun MenuCategorySelect(category: String) {
//
//    var isSelected by remember { mutableStateOf(false) }
//
//    MenuCategoryBlock(
//        onClick = {
//            // 클릭 시 상태 변경
//            isSelected = !isSelected
//        },
//        selected = isSelected,
//        category = category
//    )
//}
//
//@Composable
//fun MenuCategory() {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(87.dp)
//            .background(color = greyBackgroundColor)
//            .padding(horizontal = 24.dp, vertical = 12.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth(),
//            horizontalArrangement = Arrangement.spacedBy(12.dp)
//        ) {
//            Box(
//                modifier = Modifier.weight(1f).fillMaxHeight()
//            ){
//                MenuCategorySelect("커피")
//            }
//            Box(
//                modifier = Modifier.weight(1f).fillMaxHeight()
//            ){
//                MenuCategorySelect("차")
//            }
//            Box(
//                modifier = Modifier.weight(1f).fillMaxHeight()
//            ){
//                MenuCategorySelect("에이드")
//            }
//            Box(
//                modifier = Modifier.weight(1f).fillMaxHeight()
//            ){
//                MenuCategorySelect("음료")
//            }
//        }
//    }
//}
//
//@Composable
//fun CartMenuImage(imageURL:String) {
//    Box(
//        modifier = Modifier
//            .width(272.dp)
//            .height(272.dp)
//            .background(color = lightColor, shape = RoundedCornerShape(size = 1000.dp))
//    ) {
//        Image(
//            painter = rememberAsyncImagePainter(model = imageURL),
//            contentDescription = "Web Image",
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(CircleShape)
//                .background(color = Color.Transparent),
//            contentScale = ContentScale.Crop
//        )
//    }
//}
//
//@Composable
//fun CartMenuDescription(name: String, option: String) {
//    Column(
//        modifier = Modifier
//            .fillMaxWidth(),
//        verticalArrangement = Arrangement.Center // 세로 중앙 정렬
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentAlignment = Alignment.CenterStart
//        ) {
//            Text(
//                text = name,
//                style = h6sb
//            )
//        }
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            contentAlignment = Alignment.CenterStart
//        ) {
//            Text(
//                text = option,
//                style = body2.copy(color = titleTextColor.copy(alpha = 0.5f))
//            )
//        }
//    }
//}
//
//@Composable
//fun CartAmountPrice(amount: Int, price: Int) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .fillMaxHeight(),
//        horizontalArrangement = Arrangement.SpaceBetween,
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        OptionAmount(amount)
//        Text(
//            text = "${price}원",
//            style = h6
//        )
//    }
//}
//
//@Composable
//fun CartMenu(name: String, option: String, amount: Int, price: Int) {
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(150.dp)
//            .padding(horizontal = 24.dp)
//            .background(whiteBackgroundColor),
//
//        ){
//
//        Image(
//            modifier = Modifier
//                .width(20.dp)
//                .height(20.dp)
//                .align(Alignment.TopStart)
//                .zIndex(1f),
//            painter = painterResource(R.drawable.checkbox),
//            contentDescription = "checkbox"
//        )
//        Image(
//            modifier = Modifier
//                .width(20.dp)
//                .height(20.dp)
//                .align(Alignment.TopEnd)
//                .zIndex(1f),
//            painter = painterResource(R.drawable.x_circle),
//            contentDescription = "x in circle"
//        )
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//        ) {
//            Box(){
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(111.dp),
//                    horizontalArrangement = Arrangement.spacedBy(8.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    val imageURL = "https://fibercreme.com/wp-content/uploads/2024/10/Sub-1.jpg"
//                    CartMenuImage(imageURL)
//                    CartMenuDescription("아메리카노", "ICE, 얼음 많이")
//                }
//            }
//            CartAmountPrice(1, 2000)
//        }
//    }
//}
//
//@Composable
//fun Spacer(interval:Int = 0){
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(interval.dp)
//            .background(greyBackgroundColor)
//    )
//}