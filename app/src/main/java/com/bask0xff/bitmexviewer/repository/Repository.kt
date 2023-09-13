package com.bask0xff.bitmexviewer.repository

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

class Repository {
    private val okHttpClient = OkHttpClient()

    fun initializeWebSocket(symbol: String, listener: WebSocketListener) {
        val request = Request.Builder()
            .url("wss://www.bitmex.com/realtime?subscribe=trade:$symbol")
            .build()
        okHttpClient.newWebSocket(request, listener)
    }
}
