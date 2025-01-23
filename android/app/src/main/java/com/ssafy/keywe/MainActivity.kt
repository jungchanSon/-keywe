package com.ssafy.keywe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultAppBar
import com.ssafy.keywe.common.app.DefaultDialog
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.common.app.DefaultTextFormField
import com.ssafy.keywe.common.order.MenuCategory
import com.ssafy.keywe.common.order.MenuCategoryScreen
import com.ssafy.keywe.common.order.MenuCategorySelect
import com.ssafy.keywe.common.order.MenuSix
import com.ssafy.keywe.common.order.Spacer
import com.ssafy.keywe.order.MenuProfile
import com.ssafy.keywe.ui.theme.KeyWeTheme
import com.ssafy.keywe.ui.theme.primaryColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KeyWeTheme {
                MenuDetailScreen()
            }
        }
    }
}

@Composable
fun MenuDetailScreen() {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기") }, modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.Top
        ) {
            MenuProfile()
            Spacer(24)
        }
    }
}

@Composable
fun MenuScreen() {
    Scaffold(
        topBar = { DefaultAppBar(title = "주문하기") }, modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                elevation = FloatingActionButtonDefaults.elevation(Dp(0F)),
                containerColor = primaryColor,
                shape = CircleShape
            ) {
                Image(
                    modifier = Modifier,
                    painter = painterResource(R.drawable.rounded_shopping_cart_24),
                    contentDescription = "Cart Button"
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(bottom = 32.dp)
        ) {
            MenuCategory()
            MenuCategoryScreen()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KeyWeTheme {

    }
}