package com.bask0xff.bitmexviewer.utils

import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.model.TradeData
import com.bask0xff.bitmexviewer.model.TradeMessage
import com.google.gson.Gson

class Utils {

    companion object {
        fun parseMessage(message: String): TradeMessage {
            val gson = Gson()
            return gson.fromJson(message, TradeMessage::class.java)
        }

        fun tradeDataToTickers(tradeDataList: List<TradeData>): List<Ticker> {
            return tradeDataList.map { tradeData ->
                Ticker(
                    timestamp = tradeData.timestamp,
                    symbol = tradeData.symbol,
                    price = tradeData.price
                )
            }
        }
    }
}