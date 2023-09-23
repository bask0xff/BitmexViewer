package com.bask0xff.bitmexviewer.utils

class Constants {

    companion object {
        val websocketUrl = "wss://www.bitmex.com/realtime"
        val websocketOrderBookUrl = "$websocketUrl?subscribe=instrument,#orderBook#:#symbol#"
    }
}