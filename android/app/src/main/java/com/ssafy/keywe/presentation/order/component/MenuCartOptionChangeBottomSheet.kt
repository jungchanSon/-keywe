package com.ssafy.keywe.presentation.order.component

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.common.app.BottomButton
import com.ssafy.keywe.common.app.DefaultModalBottomSheet
import com.ssafy.keywe.presentation.order.viewmodel.MenuCartViewModel
import com.ssafy.keywe.ui.theme.contentTextColor
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.primaryColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.subtitle2
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionChangeBottomSheet(
    cartItem: MenuCartViewModel.CartItem,
    viewModel: MenuCartViewModel,
    onDismiss: () -> Unit,
    storeId: Long,
    keyWeViewModel: KeyWeViewModel,
    isKiosk: Boolean = false,
    extraOptions: MutableMap<Long, Pair<String, Int>>
) {

    // ✅ 임시 상태: tempOptions
    val tempOptions = remember { mutableStateMapOf<Long, Pair<String, Int>>() }

    // ✅ BottomSheet가 열릴 때 tempOptions 초기화
    LaunchedEffect(Unit) {
        tempOptions.clear()
        tempOptions.putAll(extraOptions)
    }

    val menu by viewModel.selectedDetailMenu.collectAsState()

    // 데이터 가져오기
    LaunchedEffect(cartItem.menuId) {
        viewModel.fetchMenuDetailById(cartItem.menuId, storeId)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val selectedSize = remember { mutableStateOf(cartItem.size) }
    val selectedTemperature = remember { mutableStateOf(cartItem.temperature) }

    val commonOptionList = menu?.options?.filter { it.optionType == "Common" } ?: emptyList()
    val sizeOptions = commonOptionList.find { it.optionName.equals("size", ignoreCase = true) }
    val temperatureOptions =
        commonOptionList.find { it.optionName.contains("temp", ignoreCase = true) }
    val sizeValues =
        sizeOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Tall", "Grande", "Venti")
    val temperatureValues =
        temperatureOptions?.optionsValueGroup?.map { it.optionValue } ?: listOf("Hot", "Ice")

    val extraOptionList = menu?.options?.filter { it.optionType == "Extra" } ?: emptyList()

    val totalPrice = remember {
        derivedStateOf {
            val menuPrice = menu?.menuPrice ?: 0
            val sizePrice = viewModel.sizePriceMap[selectedSize.value] ?: 0
            val extraOptionPrice = extraOptions.entries.sumOf { (_, pair) ->
                val optionPrice = extraOptionList.find {
                    it.optionsValueGroup.firstOrNull()?.optionValue == pair.first
                }?.optionPrice ?: 0
                optionPrice * pair.second
            }
            menuPrice + sizePrice + extraOptionPrice
        }
    }

    val coroutineScope = rememberCoroutineScope() // CoroutineScope 추가

    fun MutableMap<Long, Pair<String, Int>>.deepCopy(): MutableMap<Long, Pair<String, Int>> {
        return this.mapValues { (_, value) ->
            value.first to value.second
        }.toMutableMap()
    }


    // 초기 상태 저장
    val initialOptions = remember { extraOptions.deepCopy() }

    // BottomSheet가 열릴 때 초기 상태 저장
    LaunchedEffect(Unit) {
        initialOptions.clear()
        initialOptions.putAll(extraOptions.deepCopy())
    }

    LaunchedEffect(Unit) {
        // tempOptions 제거하고 extraOptions만 사용
        viewModel.fetchMenuDetailById(cartItem.menuId, storeId)
    }


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        // 🔥 isKiosk가 true일 때만 터치 이벤트를 막음
        if (isKiosk) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .pointerInput(Unit) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent(PointerEventPass.Initial)
                                // 🔥 모든 터치 이벤트 소비하여 터치가 작동하지 않도록
                                event.changes.forEach { it.consume() }
                            }
                        }
                    }
            )
        }
        DefaultModalBottomSheet(content = {
            Box(
                modifier = Modifier
                    .then(if (isKiosk) {
                        Modifier
//                            .border(
//                                width = 2.dp,
//                                color = primaryColor,
//                                shape = RoundedCornerShape(8.dp)
//                            )
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event =
                                            awaitPointerEvent(PointerEventPass.Initial) // 모든 터치 이벤트 감지
                                        event.changes.forEach { it.consume() } // 터치 이벤트 소비하여 상위로 전달되지 않게 만듦
                                    }
                                }
                            }
//
                    } else {
                        Modifier // isKiosk가 false일 경우 추가적인 Modifier 없음
                    })
            ){
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
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(whiteBackgroundColor) // 원하는 배경색 적용
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                MenuDetailCommonOptionRow(
                                    sizeValues,
                                    selectedSize.value,
                                    onSelect = { selectedSize.value = it }, isKiosk = isKiosk,
                                    keyWeViewModel
                                )
                                MenuDetailCommonOptionRow(
                                    temperatureValues,
                                    selectedTemperature.value,
                                    onSelect = { selectedTemperature.value = it }, isKiosk = isKiosk,
                                    keyWeViewModel
                                )
                            }
                        }
                    }

                    item {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "추가 옵션", fontSize = 16.sp, fontWeight = FontWeight.Bold)

                            Column(
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                extraOptionList.forEach { option ->
                                    option.optionsValueGroup.forEach { optionValue ->
                                        val optionId = optionValue.optionValueId
                                        val optionName = optionValue.optionValue

                                        OptionBox(
                                            id = optionId,
                                            name = optionName,
                                            optionPrice = option.optionPrice,
                                            extraOptions = tempOptions,
//                                    extraOptions = extraOptions,
                                            onOptionSelected = { id, _, count ->
                                                val optionValue =
                                                    extraOptionList.flatMap { it.optionsValueGroup }
                                                        .find { it.optionValueId == id }?.optionValue
                                                        ?: "Unknown"

                                                if (count == 0) extraOptions.remove(id)
                                                else extraOptions[id] = optionValue to count
                                            },
                                            isKiosk = isKiosk,
                                            keyWeViewModel = keyWeViewModel
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(whiteBackgroundColor)
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .height(36.dp)
                        .background(
                            color = greyBackgroundColor, shape = RoundedCornerShape(8.dp)
                        ), contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "가격", style = subtitle1, color = contentTextColor
                        )
                        Text(
                            text = "${totalPrice.value}원",
                            style = subtitle2,
                            color = contentTextColor
                        )
                    }
                }
            }
        }, onDismissRequest = {
            extraOptions.clear()
            extraOptions.putAll(initialOptions.deepCopy())
            coroutineScope.launch {
                sheetState.hide() // 슬라이딩 애니메이션으로 닫기
                onDismiss()
            }
            if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseBottomSheet)
        }, sheetState = sheetState, buttons = {
            Box(
                modifier = Modifier
                    .then(if (isKiosk) {
                        Modifier
//                            .border(
//                                width = 2.dp,
//                                color = primaryColor,
//                                shape = RoundedCornerShape(8.dp)
//                            )
                            .pointerInput(Unit) {
                                awaitPointerEventScope {
                                    while (true) {
                                        val event =
                                            awaitPointerEvent(PointerEventPass.Initial) // 모든 터치 이벤트 감지
                                        event.changes.forEach { it.consume() } // 터치 이벤트 소비하여 상위로 전달되지 않게 만듦
                                    }
                                }
                            }
//
                    } else {
                        Modifier // isKiosk가 false일 경우 추가적인 Modifier 없음
                    })
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                        .padding(horizontal = 24.dp)
                        .background(whiteBackgroundColor),
                    horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally)
                ) {
                    BottomButton(
                        modifier = Modifier
                            .weight(1f)
                            .semantics {
                                contentDescription = "close_bottom_sheet"
                            },
                        content = "취소",
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismiss()

                                // ✅ 초기 상태로 복원
                                extraOptions.clear()
                                extraOptions.putAll(initialOptions.deepCopy())
                            }
                            if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartCloseBottomSheet)
                        },
                        colors = ButtonColors(
                            containerColor = greyBackgroundColor,
                            contentColor = titleTextColor,
                            disabledContentColor = polishedSteelColor,
                            disabledContainerColor = greyBackgroundColor
                        )
                    )

                    BottomButton(
                        modifier = Modifier
                            .weight(1f)
                            .semantics {
                                contentDescription = "accept_bottom_sheet"
                            },
                        content = "수정",
                        onClick = {
                            coroutineScope.launch {
                                sheetState.hide()
                                onDismiss()

                                // ✅ extraOptions에 수정 사항 반영
                                viewModel.updateCartItem(
                                    cartItem.id,
                                    cartItem.menuId,
                                    selectedSize.value,
                                    selectedTemperature.value,
                                    extraOptions.mapValues { (_, pair) -> pair.second },
                                    storeId
                                )

                                // ✅ 수정 완료 이벤트 전송
                                if (!isKiosk) keyWeViewModel.sendButtonEvent(KeyWeButtonEvent.CartAcceptBottomSheet)
                            }
                        }
                    )
                }
            }
        })
    }
}