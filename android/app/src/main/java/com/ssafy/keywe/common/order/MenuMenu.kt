package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.pretendardkr
import com.ssafy.keywe.ui.theme.titleTextColor
import org.intellij.lang.annotations.JdkConstants.FontStyle
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@Composable
fun MenuMenu(menuName:String, menuRecipe:String, menuPrice:Int, imageURL:String) {

    Box(
        modifier = Modifier,
//        verticalAlign = Bottom
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .width(135.dp)
                .height(190.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MenuImage(imageURL)
                MenuDescription(menuName, menuRecipe, menuPrice)
            }
        }
        Image(
            modifier = Modifier
                .width(146.dp)
                .height(130.dp)
                .zIndex(-1f),
            painter = painterResource(R.drawable.menu_background_rectangle),
            contentDescription = "menu background rectangle"
        )
    }

    Box(
        modifier = Modifier
            .padding(bottom = 15.dp)
            .width(135.dp)
            .height(190.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            MenuImage(imageURL)
            MenuDescription(menuName, menuRecipe, menuPrice)
        }
    }
}

@Composable
fun MenuMenuList(menuList: List<Quadruple<String, String, Int, String>>) {
    Box(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(24.dp),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
//            verticalArrangement = Arrangement.SpaceBetween
        ) {
            items(menuList.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowItems.forEach { menu ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            contentAlignment = Alignment.Center
                        ) {
                            MenuMenuScreen(menuName = menu.first, menuRecipe = menu.second, menuPrice = menu.third, imageURL = menu.fourth)
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
fun MenuMenuScreen(menuName:String, menuRecipe:String, menuPrice:Int, imageURL:String) {
//    val ImageURL =
//        "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"
//    val MenuName = "아메리카노"
//    val MenuRecipe = "커피+물"
//    val MenuPrice = 2000

    Box(
        modifier = Modifier
            .width(145.5.dp)
            .height(190.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .padding(bottom = 15.dp)
                .width(135.dp)
                .height(190.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                MenuImage(imageURL)
                MenuDescription(menuName, menuRecipe, menuPrice)
            }
        }
        Image(
            modifier = Modifier
                .width(146.dp)
                .height(130.dp)
                .zIndex(-1f),
            painter = painterResource(R.drawable.menu_background_rectangle),
            contentDescription = "menu background rectangle"
        )
    }
}