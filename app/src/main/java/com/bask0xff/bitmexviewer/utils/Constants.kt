package com.bask0xff.bitmexviewer.utils

class Constants {

    companion object {
        val websocketUrl = "wss://www.bitmex.com/realtime"
        val websocketOrderBookUrl = "wss://ws.bitmex.com/realtime?subscribe=instrument,#orderBook#:#symbol#"
    }
}