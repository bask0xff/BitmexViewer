package com.bask0xff.bitmexviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.view.TickerAdapter
import com.bask0xff.bitmexviewer.viewmodel.MainViewModel

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
    }
}