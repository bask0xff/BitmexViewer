package com.bask0xff.bitmexviewer.model

import com.google.gson.annotations.SerializedName

data class OrderBookModel(
    @SerializedName("table")
    val table: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("data")
    val data: List<OrderBookData>
)

data class OrderBookData(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("id")
    val id: Long,
    @SerializedName("side")
    val side: String,
    @SerializedName("size")
    val size: Int,
    @SerializedName("price")
    val price: Double,
    @SerializedName("timestamp")
    val timestamp: String
)

