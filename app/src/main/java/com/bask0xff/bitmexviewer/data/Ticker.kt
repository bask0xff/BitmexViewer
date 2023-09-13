package com.bask0xff.bitmexviewer.data

data class Ticker(
    val timestamp: String,
    val symbol: String,
    val price: Double,
    var prevPrice: Double? = null
) {
    var hasUpdated = false
}