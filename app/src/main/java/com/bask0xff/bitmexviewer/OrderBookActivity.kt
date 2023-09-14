package com.bask0xff.bitmexviewer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.data.OrderBook
import com.bask0xff.bitmexviewer.viewmodel.MainViewModel

class OrderBookActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: OrderBookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_book)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setupRecyclerView()
        setupObservers()

        val symbol = intent.getStringExtra("SYMBOL")
        if (symbol != null) {
            viewModel.initializeWebSocket(symbol)
        }
    }

    private fun setupRecyclerView() {
        adapter = OrderBookAdapter()
        findViewById<RecyclerView>(R.id.recyclerView2).adapter = adapter
    }

    private fun setupObservers() {
        viewModel.orderBookLiveData.observe(this) { orderBook ->
            adapter.submitList(orderBook)
        }
    }
}

class OrderBookAdapter : ListAdapter<OrderBook, OrderBookAdapter.OrderBookViewHolder>(OrderBookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderBookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_book, parent, false)
        return OrderBookViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderBookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderBookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(orderBook: OrderBook) {

        }
    }

    class OrderBookDiffCallback : DiffUtil.ItemCallback<OrderBook>() {
        override fun areItemsTheSame(oldItem: OrderBook, newItem: OrderBook): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: OrderBook, newItem: OrderBook): Boolean {
            return oldItem == newItem
        }
    }
}
