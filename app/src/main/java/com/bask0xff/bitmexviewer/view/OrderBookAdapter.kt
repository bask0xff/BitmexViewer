package com.bask0xff.bitmexviewer.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.R
import com.bask0xff.bitmexviewer.model.OrderBookData

class OrderBookAdapter : ListAdapter<OrderBookData, OrderBookAdapter.ViewHolder>(OrderBookDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val sizeTextView: TextView = itemView.findViewById(R.id.sizeTextView)
        private val sideTextView: TextView = itemView.findViewById(R.id.sideTextView)

        fun bind(orderBookData: OrderBookData) {
            priceTextView.text = orderBookData.price.toString()
            sizeTextView.text = orderBookData.size.toString()
            if(orderBookData.side != null) sideTextView.text = orderBookData.side
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order_book, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderBookDiffCallback : DiffUtil.ItemCallback<OrderBookData>() {
        override fun areItemsTheSame(oldItem: OrderBookData, newItem: OrderBookData): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OrderBookData, newItem: OrderBookData): Boolean = oldItem == newItem
    }
}

