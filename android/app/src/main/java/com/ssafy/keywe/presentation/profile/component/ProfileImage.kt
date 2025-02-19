package com.ssafy.keywe.presentation.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.R
import com.ssafy.keywe.presentation.order.component.Base64Image
import com.ssafy.keywe.ui.theme.greyBackgroundColor
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


//@Composable
//fun Profile(
//    modifier: Modifier = Modifier,
//    name: String,
//    profileImage: String? // Base64 문자열이 전달
//) {
//    val context = LocalContext.current
//    val imageModel = when {
//        profileImage.isNullOrEmpty() -> R.drawable.humanimage
//        profileImage.startsWith("data:image") -> profileImage // Base64 처리
//        profileImage.startsWith("http") -> profileImage // URL 처리
//        else -> R.drawable.humanimage // 기본 이미지 적용
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        AsyncImage(
//            model = ImageRequest.Builder(context)
//                .data(imageModel)
//                .crossfade(true)
//                .build(),
//            contentDescription = "Profile Image",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier.size(120.dp)
//        )
//
//        Text(
//            text = name,
//            style = subtitle2,
//            modifier = Modifier
//                .padding(top = 8.dp)
//                .wrapContentSize(),
//            textAlign = TextAlign.Center
//        )
//    }
//}
@Composable
fun Profile(
    name: String,
    profileImage: String?, // Base64 문자열이 전달됨
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(color = greyBackgroundColor)
        ) {
            if (!profileImage.isNullOrBlank()) {
                Base64Image(base64String = profileImage, modifier = Modifier.size(120.dp))
            } else {
                Image(
                    painter = painterResource(id = R.drawable.humanimage),
                    contentDescription = "기본 프로필 이미지",
                    modifier = Modifier.size(120.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = subtitle2
        )
    }
}
