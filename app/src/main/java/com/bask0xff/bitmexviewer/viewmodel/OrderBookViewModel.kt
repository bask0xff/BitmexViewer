package com.bask0xff.bitmexviewer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bask0xff.bitmexviewer.model.OrderBookData

class OrderBookViewModel : ViewModel() {

    val orderBookLiveData = MutableLiveData<List<OrderBookData>>()

    fun updateOrderBookData(data: List<OrderBookData>) {
        orderBookLiveData.postValue(data)
    }

}
