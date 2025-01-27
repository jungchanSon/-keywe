package com.ssafy.keywe.common.order

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MenuSubCategory(text:String){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 22.dp, horizontal = 24.dp)
    ){
        Text(text)
    }
}