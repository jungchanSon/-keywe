package com.ssafy.keywe.presentation.order.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6
import com.ssafy.keywe.ui.theme.orangeColor
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@Composable
fun MenuDetailCommonOption(
    onSizeSelected: (String) -> Unit,
    onTemperatureSelected: (String) -> Unit
) {
    val sizeList = listOf("Tall", "Grande", "Venti")
    val temperatureList = listOf("Hot", "Ice")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = greyBackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MenuDetailCommonOptionRow(sizeList, onSizeSelected)
            MenuDetailCommonOptionRow(temperatureList, onTemperatureSelected)

        }
    }

}

@Composable
fun MenuDetailCommonOptionRow(options: List<String>, onSelect: (String) -> Unit) {
    var selectedOption by remember { mutableStateOf(options.first()) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        options.forEach { option ->
            MenuDetailCommonOptionButton(
                option = option,
                modifier = Modifier.weight(1f),
                isSelected = selectedOption == option,
                onSelect = {
                    selectedOption = option
                    onSelect(option)
                }
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

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(whiteBackgroundColor)
            .border(
                width = 2.dp,
                color = if (isSelected) orangeColor else whiteBackgroundColor,
                shape = RoundedCornerShape(10.dp)
            )
    ) {
        Button(
            onClick = onSelect,
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = titleTextColor
            ),
            elevation = ButtonDefaults.buttonElevation(0.dp)
        ) {
            Text(
                text = option,
                style = h6.copy(fontSize = 16.sp)
            )
        }
    }
}