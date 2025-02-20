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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor
import com.ssafy.keywe.webrtc.data.KeyWeButtonEvent
import com.ssafy.keywe.webrtc.viewmodel.KeyWeViewModel

@Composable
fun MenuDetailCommonOption(
    sizeOptions: List<String>,
    temperatureOptions: List<String>,
    selectedSize: String = "Tall",
    selectedTemperature: String = "Ice",
    onSizeSelected: (String) -> Unit,
    onTemperatureSelected: (String) -> Unit,
    isKiosk: Boolean = false,
    keyWeViewModel: KeyWeViewModel = hiltViewModel()
) {

//    val sizeList = listOf("Tall", "Grande", "Venti")
//    val temperatureList = listOf("Hot", "Ice")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(greyBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuDetailCommonOptionRow(sizeOptions, selectedSize, onSizeSelected, isKiosk, keyWeViewModel)
            MenuDetailCommonOptionRow(temperatureOptions, selectedTemperature, onTemperatureSelected, isKiosk, keyWeViewModel)

        }
    }

}

@Composable
fun MenuDetailCommonOptionRow(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit,
    isKiosk: Boolean = false,
    keyWeViewModel: KeyWeViewModel
) {

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        options.forEach { option ->
            MenuDetailCommonOptionButton(
                option = option,
                modifier = Modifier.weight(1f).height(45.dp),
                isSelected = selected == option,
                onSelect = {
                    onSelect(option)
                    if (!isKiosk) keyWeViewModel.sendButtonEvent(
                        KeyWeButtonEvent.MenuDetailSelectCommonOption(
                            option
                        )
                    )
                           },
            )
        }
    }
}

@Composable
fun MenuDetailCommonOptionButton(
    option: String,
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    Log.d("DetailCommonOptionName", "$option")
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .semantics { contentDescription = "select_common_option_$option" }
            .clip(RoundedCornerShape(10.dp))
            .background(whiteBackgroundColor)
            .border(
                width = 2.dp,
                color = if (isSelected) orangeColor else polishedSteelColor,
                shape = RoundedCornerShape(10.dp)
            )
            .noRippleClickable {
                onSelect()
            } // 클릭 이벤트 적용
            .padding(vertical = 8.dp, horizontal = 16.dp), // 패딩 추가하여 버튼 크기 조정
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = option,
            style = h6.copy(fontSize = 16.sp),
            color = titleTextColor
        )
    }
}
