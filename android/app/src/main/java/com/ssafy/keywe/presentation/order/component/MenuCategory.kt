package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.primaryColor

@Composable
fun MenuCategoryScreen(
    viewModel: MenuViewModel = hiltViewModel(),
    storeId: Long,
) {
    val menuCategoryList = viewModel.categories.collectAsState().value
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    Column(modifier = Modifier.fillMaxWidth()) {
        ScrollableTabRow(
            selectedTabIndex = menuCategoryList.indexOfFirst { it.categoryName == selectedCategory },
            containerColor = Color.Transparent,
            contentColor = primaryColor,
            edgePadding = 16.dp, // % 기반으로 조정
            indicator = { tabPositions ->
                val index = menuCategoryList.indexOfFirst { it.categoryName == selectedCategory }
                if (index != -1) {
                    Box(
                        Modifier
                            .tabIndicatorOffset(tabPositions[index])
                            .fillMaxWidth(0.9f)  // 너비를 90% 비율로 조정
                            .fillMaxHeight(0.005f) // 높이를 0.5% 비율로 조정
                            .background(primaryColor)
                    )
                }
            }
        ) {
            menuCategoryList.forEach { category ->
                Tab(
                    selected = selectedCategory == category.categoryName,
                    onClick = { viewModel.setSelectedCategory(category.categoryName, storeId) },
                    text = {
                        Text(
                            text = category.categoryName,
                            color = if (selectedCategory == category.categoryName) primaryColor else Color.Gray,
                            modifier = Modifier.fillMaxWidth(0.9f) // 90% 비율 적용
                        )
                    }
                )
            }
        }
    }
}
