package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.R
import com.ssafy.keywe.domain.order.OptionsModel
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.noRippleClickable
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@Composable
fun MenuDetailExtraOption(
    options: List<OptionsModel>,
    onOptionSelected: (Long, String, Int) -> Unit
) {

    val extraOptions = remember { mutableStateMapOf<Long, Pair<String, Int>>() }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 205.dp)
            .background(greyBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 24.dp)
                .padding(bottom = 110.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "추가 옵션", style = h6.copy(fontSize = 16.sp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top),
                ) {
                    options.forEach { option ->
                        val optionId = option.optionsValueGroup.firstOrNull()?.optionValueId ?: option.optionId
                        val optionName = option.optionsValueGroup.firstOrNull()?.optionValue ?: option.optionName

                        OptionBox(
                            id = optionId,
                            name = optionName,
                            optionPrice = option.optionPrice,
                            extraOptions = extraOptions,
                            onOptionSelected = { id, name, count ->
                                if (count == 0) extraOptions.remove(id)
                                else extraOptions[id] = name to count
                                onOptionSelected(id, name, count)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Option(name: String, optionPrice: Int) {
    Text(
        text = if (optionPrice == 0) name else "$name + ${optionPrice}원",
        style = subtitle1
    )
}

@Composable
fun OptionAmount(optionAmount: Int, onDecrease: () -> Unit, onIncrease: () -> Unit) {
    Row(
        modifier = Modifier
            .width(88.dp)
            .fillMaxHeight(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .noRippleClickable { onDecrease() },
            painter = painterResource(R.drawable.minus_circle),
            contentDescription = "minus in circle"
        )
        Text(
            text = "$optionAmount",
            style = subtitle1
        )
        Image(
            modifier = Modifier
                .width(20.dp)
                .height(20.dp)
                .noRippleClickable { onIncrease() },
            painter = painterResource(R.drawable.plus_circle),
            contentDescription = "plus in circle"
        )
    }
}

@Composable
fun OptionBox(
    id: Long,
    name: String,
    optionPrice: Int,
    extraOptions: MutableMap<Long, Pair<String, Int>>,
    onOptionSelected: (Long, String, Int) -> Unit
) {

    val optionAmount = remember { mutableIntStateOf(extraOptions[id]?.second ?: 0) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(
                color = whiteBackgroundColor,
                shape = RoundedCornerShape(size = 8.dp)
            )
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Option(name, optionPrice)
            OptionAmount(
                optionAmount.value,
                onDecrease = {
                    if (optionAmount.value > 0) {
                        optionAmount.value -= 1
                        extraOptions[id] = name to optionAmount.value
                        onOptionSelected(id, name, optionAmount.intValue)
                    }
                }, onIncrease = {
                    if (optionAmount.value < 9) {
                        optionAmount.value += 1
                        extraOptions[id] = name to optionAmount.value
                        onOptionSelected(id, name, optionAmount.value)
                    }
                })
        }
    }
}