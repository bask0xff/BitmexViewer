package com.bask0xff.bitmexviewer

import android.content.Intent
import android.graphics.Color
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
import com.bask0xff.bitmexviewer.data.Ticker
import com.bask0xff.bitmexviewer.model.TradeData
import com.bask0xff.bitmexviewer.model.TradeMessage
import com.bask0xff.bitmexviewer.viewmodel.MainViewModel
import com.google.gson.Gson
import okhttp3.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TickerAdapter
    private var recyclerView: RecyclerView? = null

    val tickerList = listOf(
        "XBTUSD",
        "ETHUSD",
        "LTCUSD",
        "BCHUSD",
        "XRPUSD",
        "ADAUSD",
        "EOSUSD",
        "TRXUSD",
        "ZECUSD",
        "BMEXUSD"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()
        setupObservers()


        val websocketUrl = "wss://www.bitmex.com/realtime"
        val request = Request.Builder().url(websocketUrl).build()
        val websocket = OkHttpClient().newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                Log.d("WebSocket", "Connection opened")

                for (ticket in tickerList) {
                    webSocket.send("{\"op\": \"subscribe\", \"args\": [\"trade:$ticket\"]}")
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                Log.d("WebSocket", "Received message: $text")

                val tradeMessage = parseMessage(text)
                Log.d("WebSocket", "tradeMessage: $tradeMessage")

                if(tradeMessage.table != null) {
                    val tickers = tradeDataToTickers(tradeMessage.data)
                    Log.d(TAG, "tickers: $tickers")



                    val newTickers = tradeDataToTickers(tradeMessage.data)
                    val currentTickers = viewModel.tickersLiveData.value ?: emptyList()

                    newTickers.forEach { newTicker ->
                        currentTickers.find { it.symbol == newTicker.symbol }?.let { currentTicker ->
                            newTicker.prevPrice = currentTicker.price
                        }
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

    private fun parseMessage(message: String): TradeMessage {
        val gson = Gson()
        return gson.fromJson(message, TradeMessage::class.java)
    }

    private fun tradeDataToTickers(tradeDataList: List<TradeData>): List<Ticker> {
        return tradeDataList.map { tradeData ->
            Ticker(
                timestamp = tradeData.timestamp,
                symbol = tradeData.symbol,
                price = tradeData.price
            )
        }
    }

    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: ")

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        adapter = TickerAdapter { symbol ->
            val intent = Intent(this, OrderBookActivity::class.java)
            intent.putExtra("SYMBOL", symbol)
            startActivity(intent)
        }
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = LinearLayoutManager(this)

        viewModel.tickersLiveData.observe(this) { tickers ->
            adapter.submitList(tickers)
        }
    }

    private fun setupObservers() {
        viewModel.tickerLiveData.observe(this) { tickers ->
            Log.d(TAG, "setupObservers: Update adapter with new data")
            adapter.submitList(tickers)
        }
    }
}

class TickerAdapter(private val onClick: (String) -> Unit) : ListAdapter<Ticker, TickerAdapter.TickerViewHolder>(TickerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ticker, parent, false)
        return TickerViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        val ticker = getItem(position)
        holder.bind(ticker)
    }

    class TickerViewHolder(itemView: View, private val onClick: (String) -> Unit) : RecyclerView.ViewHolder(itemView) {

        fun bind(ticker: Ticker) {

            itemView.findViewById<TextView>(R.id.textViewTickerSymbol).text = ticker.symbol
            var delta = if (ticker.prevPrice != null) {ticker.price - ticker.prevPrice!!} else 0

            itemView.findViewById<TextView>(R.id.textViewLastPrice).text = ticker.price.toString() //+ " ($delta)"

            if (ticker.prevPrice != null) {
                if (ticker.price > ticker.prevPrice!!) {
                    itemView.findViewById<TextView>(R.id.textViewLastPrice).setTextColor(Color.GREEN)
                } else if (ticker.price < ticker.prevPrice!!) {
                    itemView.findViewById<TextView>(R.id.textViewLastPrice).setTextColor(Color.RED)
                } else {
                    itemView.findViewById<TextView>(R.id.textViewLastPrice).setTextColor(Color.YELLOW)
                }
            }

            itemView.findViewById<TextView>(R.id.textViewTimestamp).text = ticker.timestamp.replace("Z", "").replace("T", " ").substring(0, 16)
            itemView.setOnClickListener {
                onClick(ticker.symbol)
            }
        }

    }

    class TickerDiffCallback : DiffUtil.ItemCallback<Ticker>() {
        override fun areItemsTheSame(oldItem: Ticker, newItem: Ticker): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Ticker, newItem: Ticker): Boolean {
            return oldItem == newItem
        }
    }
}

