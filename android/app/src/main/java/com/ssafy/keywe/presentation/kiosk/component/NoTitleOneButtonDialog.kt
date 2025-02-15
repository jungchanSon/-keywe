package com.ssafy.keywe.presentation.kiosk.component

import com.ssafy.keywe.common.app.BottomButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ssafy.keywe.ui.theme.greyBackgroundColor
import com.ssafy.keywe.ui.theme.h6sb
import com.ssafy.keywe.ui.theme.polishedSteelColor
import com.ssafy.keywe.ui.theme.subtitle1
import com.ssafy.keywe.ui.theme.titleTextColor
import com.ssafy.keywe.ui.theme.whiteBackgroundColor


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoTitleOneButtonDialog(
    description: String, onCancel: () -> Unit,
    onConfirm: () -> Unit,
) {

    BasicAlertDialog(
        onDismissRequest = onCancel, properties = DialogProperties(
            dismissOnBackPress = false,
        )
    ) {
        Card(
            colors = CardColors(
                containerColor = whiteBackgroundColor,
                contentColor = titleTextColor,
                disabledContainerColor = whiteBackgroundColor,
                disabledContentColor = titleTextColor,
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = description, style = subtitle1, textAlign = TextAlign.Center)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    BottomButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        content = "확인",
                        onClick = onConfirm
                    )
                }
            }

        }
    }

}