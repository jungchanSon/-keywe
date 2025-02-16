package com.ssafy.keywe.util

import android.content.Context

object TouchCoordinateConverter {
    /**
     * 화면의 물리적 특성을 담는 데이터 클래스
     *
     * @param width 화면의 픽셀 단위 너비 (예: 1080px)
     * @param height 화면의 픽셀 단위 높이 (예: 2400px)
     * @param density 화면의 밀도 값. DisplayMetrics.density (예: 2.0 = xhdpi)
     * @param aspectRatio 화면의 종횡비 (height/width). 기본값으로 계산됨
     */
    data class ScreenMetrics(
        val width: Int,
        val height: Int,
        val density: Float,
        val aspectRatio: Float = height.toFloat() / width,
    )

    /**
     * 픽셀 단위의 값을 DP(Density-independent Pixels)로 변환
     *
     * @param px 변환할 픽셀 값
     * @param density 화면 밀도 (DisplayMetrics.density)
     * @return DP 값
     *
     * 예시:
     * - density가 2.0인 기기에서 100px -> 50dp
     * - density가 3.0인 기기에서 150px -> 50dp
     */
    fun pxToDp(px: Float, density: Float): Float {
        return px / density
    }

    /**
     * DP(Density-independent Pixels) 값을 픽셀로 변환
     *
     * @param dp 변환할 DP 값
     * @param density 화면 밀도 (DisplayMetrics.density)
     * @return 픽셀 값
     *
     * 예시:
     * - density가 2.0인 기기에서 50dp -> 100px
     * - density가 3.0인 기기에서 50dp -> 150px
     */
    fun dpToPx(dp: Float, density: Float): Float {
        return dp * density
    }

    /**
     * 송신측 터치 좌표를 수신측 화면에 맞게 변환합니다.
     * 서로 다른 밀도와 크기를 가진 화면 간의 정확한 좌표 매핑을 위해
     * DP를 사용하여 변환을 수행합니다.
     *
     * 변환 과정:
     * 1. 픽셀 -> DP 변환
     * 2. DP 기준으로 상대 좌표(0~1) 계산
     * 3. 화면 비율 차이 보정
     * 4. 타겟 화면에 맞게 DP 변환
     * 5. 최종 픽셀 좌표로 변환
     *
     * @param sourceX 송신측 화면에서의 터치 X 좌표 (픽셀)
     * @param sourceY 송신측 화면에서의 터치 Y 좌표 (픽셀)
     * @param sourceMetrics 송신측 화면 정보
     * @param targetMetrics 수신측 화면 정보
     * @return 수신측 화면에 맞게 변환된 (X, Y) 좌표 쌍 (픽셀)
     *
     * 사용 예시:
     * val (targetX, targetY) = convertCoordinates(
     *     sourceX = event.x,  // 예: 500px
     *     sourceY = event.y,  // 예: 1000px
     *     sourceMetrics = sourceMetrics,  // 예: 1080x2400, density=2.0
     *     targetMetrics = targetMetrics   // 예: 1440x2960, density=3.0
     * )
     */
    fun convertCoordinates(
        sourceX: Float,
        sourceY: Float,
        sourceMetrics: ScreenMetrics,
        targetMetrics: ScreenMetrics,
    ): Pair<Float, Float> {
        // 1. 픽셀 좌표를 DP로 변환
        val sourceDpX = pxToDp(sourceX, sourceMetrics.density)
        val sourceDpY = pxToDp(sourceY, sourceMetrics.density)

        // 2. DP 기준으로 상대 좌표 계산 (0~1 사이 값)
        val sourceWidthDp = pxToDp(sourceMetrics.width.toFloat(), sourceMetrics.density)
        val sourceHeightDp = pxToDp(sourceMetrics.height.toFloat(), sourceMetrics.density)

        val relativeX = sourceDpX / sourceWidthDp
        val relativeY = sourceDpY / sourceHeightDp

        // 3. 화면 비율 차이 보정
        // 예: 18:9 화면에서 16:9 화면으로 변환 시 수직 위치 조정
        val aspectRatioCorrection = targetMetrics.aspectRatio / sourceMetrics.aspectRatio

        // 4. 타겟 화면의 DP 크기 계산
        val targetWidthDp = pxToDp(targetMetrics.width.toFloat(), targetMetrics.density)
        val targetHeightDp = pxToDp(targetMetrics.height.toFloat(), targetMetrics.density)

        // 5. 상대 좌표를 타겟 DP로 변환
        val targetDpX = relativeX * targetWidthDp
        val targetDpY = relativeY * targetHeightDp * aspectRatioCorrection

        // 6. DP를 다시 픽셀로 변환하고 화면 범위 내로 제한
        val finalX = dpToPx(targetDpX, targetMetrics.density)
            .coerceIn(0f, targetMetrics.width.toFloat())
        val finalY = dpToPx(targetDpY, targetMetrics.density)
            .coerceIn(0f, targetMetrics.height.toFloat())

        return Pair(finalX, finalY)
    }

    /**
     * 수신된 터치 좌표의 정밀도를 높이기 위한 보정을 적용합니다.
     * DP 단위로 보정을 수행하여 서로 다른 화면 밀도에서도 일관된 보정이 이루어지도록 합니다.
     *
     * 보정 과정:
     * 1. 픽셀 -> DP 변환
     * 2. 1dp 단위로 반올림 보정
     * 3. 보정된 DP -> 픽셀 변환
     *
     * @param x 보정할 X 좌표 (픽셀)
     * @param y 보정할 Y 좌표 (픽셀)
     * @param metrics 현재 화면 정보
     * @return 보정된 (X, Y) 좌표 쌍 (픽셀)
     *
     * 사용 예시:
     * val (correctedX, correctedY) = applyTouchCorrection(
     *     x = receivedX,  // 예: 500.4px
     *     y = receivedY,  // 예: 1000.7px
     *     metrics = screenMetrics
     * )
     *
     * 참고:
     * - 1dp 미만의 오차는 반올림하여 보정
     * - 이는 눈에 보이지 않는 미세한 떨림을 방지하기 위함
     */
    fun applyTouchCorrection(
        x: Float,
        y: Float,
        metrics: ScreenMetrics,
    ): Pair<Float, Float> {
        // 픽셀 -> DP 변환
        val xDp = pxToDp(x, metrics.density)
        val yDp = pxToDp(y, metrics.density)

        // 1dp를 임계값으로 사용
        val thresholdDp = 1f

        // DP 단위에서 보정 (1dp 기준으로 반올림)
        val correctedXDp = if (xDp % thresholdDp < thresholdDp / 2) {
            xDp - (xDp % thresholdDp)
        } else {
            xDp + (thresholdDp - (xDp % thresholdDp))
        }

        val correctedYDp = if (yDp % thresholdDp < thresholdDp / 2) {
            yDp - (yDp % thresholdDp)
        } else {
            yDp + (thresholdDp - (yDp % thresholdDp))
        }

        // 보정된 DP -> 픽셀 변환
        return Pair(
            dpToPx(correctedXDp, metrics.density),
            dpToPx(correctedYDp, metrics.density)
        )
    }
}

/**
 * 현재 화면의 메트릭스 정보를 초기화합니다.
 *
 * @param context 안드로이드 Context
 * @return 현재 화면의 메트릭스 정보
 *
 * 사용 예시:
 * val screenMetrics = initializeMetrics(context)
 */
fun initializeMetrics(context: Context): TouchCoordinateConverter.ScreenMetrics {
    val displayMetrics = context.resources.displayMetrics
    return TouchCoordinateConverter.ScreenMetrics(
        width = displayMetrics.widthPixels,
        height = displayMetrics.heightPixels,
        density = displayMetrics.density
    )
}