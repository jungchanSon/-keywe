package com.ssafy.keywe.common.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuImage(imageURL:String){
//    val imageURL = "https://file.notion.so/f/f/6e800ee0-e9da-4766-b834-502cf74dc80f/f674efcc-0102-4645-ad48-801e111e90a8/%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png?table=block&id=b022cea1-d75a-4644-85c2-839109a6113a&spaceId=6e800ee0-e9da-4766-b834-502cf74dc80f&expirationTimestamp=1737720000000&signature=jOqHikoR8etsHVDbV5vIQ3J7Z2qu6sFu_8k6SlqCq6A&downloadName=%EC%95%84%EB%A9%94%EB%A6%AC%EC%B9%B4%EB%85%B8.png"

    Image(
        painter = rememberAsyncImagePainter(model = imageURL),
        contentDescription = "Web Image",
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .background(color = Color.Transparent),
        contentScale = ContentScale.Fit
    )
}