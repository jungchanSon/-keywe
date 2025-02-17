//package com.ssafy.keywe.util
//
//import androidx.compose.ui.unit.Density
//import com.ssafy.keywe.webrtc.data.ScreenSize
//
//
//object ScreenRatioUtil {
//    fun convertCoordinates(
//        sourceX: Float, sourceY: Float,
//        sourceSize: ScreenSize, targetSize: ScreenSize,
//    ): Pair<Float, Float> {
//        val relativeX = sourceX / sourceSize.x
//        val relativeY = sourceY / sourceSize.y
//
//        val targetX = relativeX * targetSize.x
//        val targetY = relativeY * targetSize.y
//
//        return Pair(targetX, targetY)
//    }
//
//
//    fun pixelToDp(pixel: Float, density: Density): Float {
//        return pixel / density.density
//    }
//
//    fun dpToPixel(dp: Float, density: Density): Float {
//        return dp * density.density
//    }
//
////    fun convertTo(screenSize: ScreenSize, density: Density) {
////        screenSize.x
////        val metrics = context.resources.displayMetrics
//////        Log.d("Screen Size PX", "density ${density.density}")
//////        Log.d("Screen Size PX", "metrics ${metrics.density}")
////        val width: Dp = with(density) { metrics.widthPixels.toDp() }
////        val height: Dp = with(density) { metrics.heightPixels.toDp() }
////
////
////
////        Log.d(
////            "Screen Size PX",
////            "width: ${metrics.widthPixels}, height: ${metrics.heightPixels}, density: $density"
////        )
////
////        Log.d("Screen Size DP", "width: $width, height: $height")
////
////        screenSize = ScreenSize(
////            MessageType.ScreenSize, width.value, height.value
////        )
////    }
//
//}
