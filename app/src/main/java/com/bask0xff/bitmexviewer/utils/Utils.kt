package com.bask0xff.bitmexviewer.utils

import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.model.TradeData
import com.bask0xff.bitmexviewer.model.TradeModel
import com.google.gson.Gson

class Utils {

    companion object {
        fun parseMessage(message: String): TradeModel {
            val gson = Gson()
            return gson.fromJson(message, TradeModel::class.java)
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