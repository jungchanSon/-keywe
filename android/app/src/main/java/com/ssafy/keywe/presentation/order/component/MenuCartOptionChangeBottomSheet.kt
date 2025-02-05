package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.presentation.order.viewmodel.CartItem
import com.ssafy.keywe.presentation.order.viewmodel.MenuViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionChangeBottomSheet(
    cartItem: CartItem, viewModel: MenuViewModel, onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedSize = remember { mutableStateOf(cartItem.size) }
    val selectedTemperature = remember { mutableStateOf(cartItem.temperature) }
    val options = remember { viewModel.getExtraOptions() }
    val extraOptions =
        remember { mutableStateMapOf<String, Int>().apply { putAll(cartItem.extraOptions) } }

    val totalPrice = remember {
        derivedStateOf {
            val menuPrice = cartItem.price // 기존 상품 가격
            val sizePrice = viewModel.sizePriceMap[selectedSize.value] ?: 0
            val extraOptionPrice = extraOptions.entries.sumOf { (name, count) ->
                val optionPrice = options.find { it.name == name }?.price ?: 0
                optionPrice * count
            }
            menuPrice + sizePrice + extraOptionPrice
        }
    }

    LaunchedEffect(viewModel.selectedCartItem.collectAsState().value) {
        viewModel.selectedCartItem.value?.let { cartItem ->
            selectedSize.value = cartItem.size
            selectedTemperature.value = cartItem.temperature
        }
    }

    DefaultModalBottomSheet(content = {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteBackgroundColor)
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp)
                .heightIn(max = 580.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    text = "옵션 변경", fontSize = 20.sp, fontWeight = FontWeight.Bold
                )
            }

            item {
                MenuDetailCommonOption(selectedSize = selectedSize.value,
                    selectedTemperature = selectedTemperature.value,
                    onSizeSelected = { selectedSize.value = it },
                    onTemperatureSelected = { selectedTemperature.value = it })
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "추가 옵션", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        options.forEach { option ->
                            OptionBox(name = option.name,
                                optionPrice = option.price,
                                extraOptions = extraOptions,
                                onOptionSelected = { name, count, _ ->
                                    if (count == 0) extraOptions.remove(name) else extraOptions[name] =
                                        count
                                })
                        }
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteBackgroundColor)
                .padding(top = 16.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "총 가격", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "${totalPrice.value}원", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }, onDismissRequest = onDismiss, sheetState = sheetState, buttons = {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .background(whiteBackgroundColor),
            horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
        ) {
            BottomButton(
                modifier = Modifier.weight(1f),
                content = "취소",
                onClick = onDismiss,
                colors = ButtonColors(
                    containerColor = greyBackgroundColor,
                    contentColor = titleTextColor,
                    disabledContentColor = polishedSteelColor,
                    disabledContainerColor = greyBackgroundColor
                ),
            )
            BottomButton(modifier = Modifier.weight(1f), content = "수정", onClick = {
                viewModel.updateCartItem(
                    cartItem.id, selectedSize.value, selectedTemperature.value, extraOptions
                )
                onDismiss()
            })
        }
    })
}
