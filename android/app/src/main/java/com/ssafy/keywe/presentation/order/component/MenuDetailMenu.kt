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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.ui.theme.h5
import com.ssafy.keywe.ui.theme.lightColor
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun MenuDetailMenu(modifier: Modifier = Modifier, menuName: String, menuPrice: Int, menuImageURL: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MenuDetailImage(menuImageURL)
            Text(
                text = menuName,
                color = primaryColor,
                style = h5.copy(fontWeight = FontWeight.ExtraBold)
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

        val decodedMenuImageUrl = Uri.decode(menuImageURL)

        Image(
            painter = rememberAsyncImagePainter(model = decodedMenuImageUrl),
            contentDescription = "Web Image",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}