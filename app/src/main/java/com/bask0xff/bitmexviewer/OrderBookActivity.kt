package com.bask0xff.bitmexviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.data.OrderBook
import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.model.TradeData
import com.bask0xff.bitmexviewer.model.TradeMessage
import com.bask0xff.bitmexviewer.utils.Constants
import com.bask0xff.bitmexviewer.utils.Utils
import com.bask0xff.bitmexviewer.viewmodel.OrderBookViewModel
import com.google.gson.Gson
import okhttp3.*

class OrderBookActivity : AppCompatActivity() {

    private val TAG = "OrderBookActivity"
    private lateinit var viewModel: OrderBookViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_book)

        viewModel = ViewModelProvider(this).get(OrderBookViewModel::class.java)

        setupObservers()

        val symbol = intent.getStringExtra("SYMBOL")
        if (symbol != null) {
            findViewById<TextView>(R.id.tickerName).text = symbol
            //viewModel.initializeWebSocket(symbol)
        }

        val request = Request.Builder().url(Constants.websocketUrl).build()
        val websocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Connection opened")

                webSocket.send("{\"op\": \"subscribe\", \"args\": [\"trade:$symbol\"]}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "WebSocket, Received message: $text")

                val tradeMessage = Utils.parseMessage(text)
                Log.d("WebSocket", "tradeMessage: $tradeMessage")

                if(tradeMessage.table != null) {
                    val tickers = Utils.tradeDataToTickers(tradeMessage.data)
                    Log.d(TAG, "tickers: $tickers")

                    val newTickers = Utils.tradeDataToTickers(tradeMessage.data)
                    val currentTickers = viewModel.tickersLiveData.value ?: emptyList()

                    currentTickers.forEach { currentTicker ->

                        currentTicker.price
                        Log.d(TAG, "onMessage: price: ${currentTicker.price}")

                        findViewById<TextView>(R.id.tickerName).text =
                            "$symbol : ${currentTicker.price}"
                    }
                    viewModel.addAndNotifyTickers(newTickers)
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e("WebSocket", "Connection failed: ${t.message}")
            }
        })
    }

    private fun setupObservers() {
        viewModel.orderBookLiveData.observe(this) { orderBook ->
            Log.d(TAG, "setupObservers: $orderBook")
        }
    }
}

