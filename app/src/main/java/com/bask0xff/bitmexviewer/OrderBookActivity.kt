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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.data.OrderBook
import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.model.ResponseModel
import com.bask0xff.bitmexviewer.model.TradeData
import com.bask0xff.bitmexviewer.model.TradeMessage
import com.bask0xff.bitmexviewer.utils.Constants
import com.bask0xff.bitmexviewer.utils.Utils
import com.bask0xff.bitmexviewer.view.OrderBookAdapter
import com.bask0xff.bitmexviewer.viewmodel.OrderBookViewModel
import com.google.gson.Gson
import okhttp3.*

class OrderBookActivity : AppCompatActivity() {

    private val TAG = "OrderBookActivity"
    private lateinit var viewModel: OrderBookViewModel
    private val orderBookLevel = "orderBookL2_25"//"orderBook10"

    private lateinit var adapter: OrderBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_book)

        viewModel = ViewModelProvider(this).get(OrderBookViewModel::class.java)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView2)
        adapter = OrderBookAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        setupObservers()

        val symbol = intent.getStringExtra("SYMBOL") ?: return

        findViewById<TextView>(R.id.tickerName).text = symbol

        val orderBookWsUrl = Constants.websocketOrderBookUrl.replace("#orderbook#", orderBookLevel).replace("#symbol#", symbol)
        val request = Request.Builder().url(orderBookWsUrl).build()
        val websocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Connection opened")

                webSocket.send("{\"op\": \"subscribe\", \"args\": [\"$orderBookLevel:$symbol\"]}")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d(TAG, "WebSocket, Received message: $text")

                try {
                    val data = Gson().fromJson(text, ResponseModel::class.java).data
                    viewModel.updateOrderBookData(data)
                }
                catch (e: Exception){

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
            Log.d(TAG, "Received orderBook data: $orderBook")
            adapter.submitList(orderBook)
        }
    }
}


