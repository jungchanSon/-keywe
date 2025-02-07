package com.ssafy.keywe.presentation.order.component

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.presentation.order.viewmodel.OrderViewModel
import com.ssafy.keywe.ui.theme.h5
import com.ssafy.keywe.ui.theme.lightColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle2

@Composable
fun MenuDetailMenu(
    modifier: Modifier = Modifier,
    menuId: Int,
    menuPrice: Int,
    viewModel: OrderViewModel
) {
    val menu = viewModel.getMenuDataById(menuId)
    val menuName = menu?.name ?: ""
    val menuImageURL = menu?.imageURL ?: ""
    val menuDescription = menu?.description ?: ""

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 230.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuDetailImage(menuImageURL)
            Text(
                text = menuName,
                color = primaryColor,
                style = h5.copy(fontWeight = FontWeight.ExtraBold)
            )
            Text (
                text = menuDescription,
                color = polishedSteelColor,
                style = subtitle2.copy(fontSize = 16.sp, letterSpacing = 0.em, lineHeight = 22.sp),
                textAlign = TextAlign.Center
            )
            Text(
                text = "$menuPrice 원",
                style = h5.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
fun MenuDetailImage(menuImageURL:String) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(color = lightColor, shape = CircleShape)
            .padding(12.dp)
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = menuImageURL),
            contentDescription = "Web Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.FillHeight
        )
    }
}