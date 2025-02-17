package com.ssafy.keywe.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssafy.keywe.R
import com.ssafy.keywe.ui.theme.subtitle2


//@Composable
//fun Profile(name: String, profileImage: String?, modifier: Modifier = Modifier) {
//    val context = LocalContext.current
//    val imageModel = when {
//        profileImage.isNullOrEmpty() -> R.drawable.humanimage
//        profileImage.startsWith("data:image") -> profileImage // Base64 처리
//        profileImage.startsWith("http") -> profileImage // URL 처리
//        else -> R.drawable.humanimage
//    }
//
//    Column(
//        modifier = modifier.padding(8.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Image(
//            painter = imagePainter,
//            contentDescription = "프로필 이미지",
//            modifier = Modifier
//                .size(80.dp)
//                .background(lightColor)
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(text = name, style = subtitle2)
//    }
//}


@Composable
fun Profile(
    modifier: Modifier = Modifier,
    name: String,
    profileImage: String? // nullable로 변경
) {
    val context = LocalContext.current
    val imageModel = when {
        profileImage.isNullOrEmpty() -> R.drawable.humanimage
        profileImage.startsWith("data:image") -> profileImage // Base64 처리
        profileImage.startsWith("http") -> profileImage // URL 처리
        else -> R.drawable.humanimage // 기본 이미지 적용
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(imageModel)
                .crossfade(true)
                .build(),
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(120.dp)
        )

        Text(
            text = name,
            style = subtitle2,
            modifier = Modifier
                .padding(top = 8.dp)
                .wrapContentSize(),
            textAlign = TextAlign.Center
        )
    }
}
