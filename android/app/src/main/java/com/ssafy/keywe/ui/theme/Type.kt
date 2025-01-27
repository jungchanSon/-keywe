package com.ssafy.keywe.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.ssafy.keywe.R

val pretendardkr = FontFamily(
    Font(R.font.pretendard_black, FontWeight.Black, FontStyle.Normal),
    Font(R.font.pretendard_extra_bold, FontWeight.ExtraBold, FontStyle.Normal),
    Font(R.font.pretendard_bold, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.pretendard_extra_light, FontWeight.ExtraLight, FontStyle.Normal),
    Font(R.font.pretendard_light, FontWeight.Light, FontStyle.Normal),
    Font(R.font.pretendard_medium, FontWeight.Medium, FontStyle.Normal),
    Font(R.font.pretendard_regular, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.pretendard_semi_bold, FontWeight.SemiBold, FontStyle.Normal),
    Font(R.font.pretendard_thin, FontWeight.Thin, FontStyle.Normal),
)

val sen = FontFamily(
    Font(R.font.sen_bold, FontWeight.Bold, FontStyle.Normal),
)

val logo = TextStyle(
    fontFamily = sen, fontWeight = FontWeight.Bold, fontSize = 36.sp,
    lineHeight = 43.sp
)

val h1 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Light,
    fontSize = 96.sp,
    letterSpacing = (-0.015).em,
)

val h2 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Light,
    fontSize = 60.sp,
    letterSpacing = (-0.005).em,
)
val h3 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 48.sp,
)
val h4 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Medium,
    fontSize = 34.sp,
    letterSpacing = (0.0025).em,
)
val h5 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 24.sp,
)
val h6 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Medium,
    fontSize = 20.sp,
    letterSpacing = (0.0015).em,
)
val h6sb = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.SemiBold,
    fontSize = 20.sp,
    letterSpacing = (0.0015).em,
)
val subtitle1 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = (0.0015).em,
)
val subtitle2 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = (0.001).em,
)
val body1 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 16.sp,
    letterSpacing = (0.005).em,
)
val body2 = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 14.sp,
    letterSpacing = (0.0025).em,
)
val button = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    letterSpacing = (0.0125).em,
)
val caption = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 12.sp,
    letterSpacing = (0.004).em,
)
val overline = TextStyle(
    fontFamily = pretendardkr, fontWeight = FontWeight.Normal,
    fontSize = 10.sp,
    letterSpacing = (0.015).em,
)


// Set of Material typography styles to start with
val Typography = Typography(

    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )

)