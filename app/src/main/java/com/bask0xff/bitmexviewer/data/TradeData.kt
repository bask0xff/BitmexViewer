package com.bask0xff.bitmexviewer.model

data class TradeMessage(
    val table: String,
    val action: String,
    val data: List<TradeData>
)

data class TradeData(
    val timestamp: String,
    val symbol: String,
    val side: String,
    val size: Int,
    val price: Double,
    val tickDirection: String,
    val trdMatchID: String,
    val grossValue: Long,
    val homeNotional: Double,
    val foreignNotional: Double,
    val trdType: String
)
