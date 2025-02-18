package com.ssafy.keywe.common.app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ssafy.keywe.ui.theme.whiteBackgroundColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultModalBottomSheet(
    content: @Composable () -> Unit,
    onDismissRequest: () -> Unit,
    sheetState: SheetState,
    buttons: @Composable RowScope.() -> Unit = {},
) {
//    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        dragHandle = null,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        containerColor = whiteBackgroundColor,
        properties = ModalBottomSheetDefaults.properties(shouldDismissOnBackPress = false),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {

        Column(
            modifier = Modifier.background(whiteBackgroundColor),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
//            modifier = Modifier.padding(horizontal = 24.dp, vertical = 40.dp)
        ) {
            content()
            Row(content = buttons)
        }
    }
}