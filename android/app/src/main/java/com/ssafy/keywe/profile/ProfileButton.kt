package com.ssafy.keywe

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.subtitle2


//@Composable
//fun MenuButton(
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit = {}
//) {
//    Button(
//        onClick = onClick,
//        modifier = modifier
//            .width(306.17383.dp)
//            .height(49.69313.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color(0xFFF6F6F6),
//            contentColor = Color.Black
//        ),
//        shape = RoundedCornerShape(8.01502.dp)
//    ) {
//        Box(
//            modifier = Modifier.fillMaxSize(),
//            contentAlignment = Alignment.CenterStart
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text(
//                    text = "Menu",
//                    style = subtitle2,
//                    modifier = Modifier
//                        .alpha(0.5f)
//                        .width(54.dp)
//                        .height(25.dp)
//                        .padding(start = 16.dp)
//                )
//
//            }
//        }
//    }
//}


@Composable
fun MenuButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFF6F6F6),
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Menu",
                style = subtitle2,
            )

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }
}
