package com.bask0xff.bitmexviewer.repository

import com.bask0xff.bitmexviewer.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocketListener

class Repository {
    private val okHttpClient = OkHttpClient()

    fun initializeWebSocket(symbol: String, listener: WebSocketListener) {
        val request = Request.Builder()
            .url("${Constants.websocketUrl}?subscribe=trade:$symbol")
            .build()
        okHttpClient.newWebSocket(request, listener)
    }
}
