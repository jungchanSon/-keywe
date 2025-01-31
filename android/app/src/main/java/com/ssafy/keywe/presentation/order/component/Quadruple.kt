package com.ssafy.keywe.presentation.order.component

data class Quadruple<A, B, C, D>(
    val name: A,
    val recipe: B,
    val price: C,
    val imageURL: D,
)