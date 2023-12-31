package com.bask0xff.bitmexviewer.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.repository.Repository
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainViewModel : ViewModel() {
    private val TAG = "MainViewModel"
    private val repository = Repository()
    val tickerLiveData = MutableLiveData<List<Ticker>>()

    private val _tickersLiveData = MutableLiveData<List<Ticker>>()
    val tickersLiveData: LiveData<List<Ticker>> get() = _tickersLiveData

    private val uniqueTickers: LinkedHashMap<String, Ticker> = LinkedHashMap()

    fun addAndNotifyTickers(newTickers: List<Ticker>) {
        for (newTicker in newTickers) {
            uniqueTickers[newTicker.symbol] = newTicker
        }

        while (uniqueTickers.size > 10) {
            uniqueTickers.remove(uniqueTickers.keys.first())
        }

        _tickersLiveData.postValue(uniqueTickers.values.toList())
    }

    fun initializeWebSocket(symbol: String) {
        Log.d(TAG, "initializeWebSocket: $symbol")
        repository.initializeWebSocket(symbol, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)

                Log.d(TAG, "onMessage: $text")
            }
        })
    }
}