package com.ssafy.keywe.webrtc

import android.content.Context
import android.util.Log
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import com.ssafy.keywe.util.TouchCoordinateConverter
import com.ssafy.keywe.util.initializeMetrics
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenSizeManager @Inject constructor(
//    private val context: Context,
//    private val density: Density,
) {
//    var screenSize: ScreenSize = ScreenSize(MessageType.ScreenSize, 0f, 0f, 0f, 0f)
//        private set

    var screenMetrics: TouchCoordinateConverter.ScreenMetrics =
        TouchCoordinateConverter.ScreenMetrics(0f, 0f, 0f, 0f)

    var statusBarHeightPx: Int = 0

//    init {
//        updateScreenSize()
//    }

    fun updateSystemSize(statusHeight: Int) {
        statusBarHeightPx = statusHeight
    }

    fun updateScreenMetrics(context: Context, height: Float, width: Float) {
        screenMetrics = initializeMetrics(context, height, width)
    }

    fun updateScreenSize(context: Context, density: Density) {
        val metrics = context.resources.displayMetrics
//        Log.d("Screen Size PX", "density ${density.density}")
//        Log.d("Screen Size PX", "metrics ${metrics.density}")
        val width: Dp = with(density) { metrics.widthPixels.toDp() }
        val height: Dp = with(density) { metrics.heightPixels.toDp() }



        Log.d(
            "Screen Size PX",
            "width: ${metrics.widthPixels}, height: ${metrics.heightPixels}, density: $density"
        )

        Log.d("Screen Size DP", "width: $width, height: $height")
//        Log.d("Screen Size DP", "Float width: ${width.value}, height: ${height.value}")
//        screenSize = ScreenSize(
//            MessageType.ScreenSize,
//            metrics.widthPixels.toFloat(),
//            metrics.heightPixels.toFloat(),
//            density.density,
//        )
//        screenSize = ScreenSize(
//            MessageType.ScreenSize, width.value, height.value
//        )
    }
}
