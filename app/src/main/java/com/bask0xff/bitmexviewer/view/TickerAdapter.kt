package com.bask0xff.bitmexviewer.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bask0xff.bitmexviewer.R
import com.bask0xff.bitmexviewer.data.Ticker

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