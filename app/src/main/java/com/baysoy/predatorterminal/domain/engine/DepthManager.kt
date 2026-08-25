package com.baysoy.predatorterminal.domain.engine

import com.baysoy.predatorterminal.data.model.DepthUpdate
import com.baysoy.predatorterminal.data.model.OrderBookSnapshot
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DepthManager {
    
    private val bids = mutableMapOf<Double, Double>()
    private val asks = mutableMapOf<Double, Double>()
    private var lastUpdateId = 0L
    private val buffer = mutableListOf<DepthUpdate>()
    private var ready = false
    
    private val _bestBid = MutableStateFlow(0.0)
    val bestBid: StateFlow<Double> = _bestBid
    
    private val _bestAsk = MutableStateFlow(Double.MAX_VALUE)
    val bestAsk: StateFlow<Double> = _bestAsk
    
    private val _imbalance = MutableStateFlow(0.0)
    val imbalance: StateFlow<Double> = _imbalance
    
    fun applySnapshot(snapshot: OrderBookSnapshot) {
        bids.clear()
        asks.clear()
        
        snapshot.bids.forEach { bid ->
            val price = bid[0].toDouble()
            val quantity = bid[1].toDouble()
            bids[price] = quantity
        }
        
        snapshot.asks.forEach { ask ->
            val price = ask[0].toDouble()
            val quantity = ask[1].toDouble()
            asks[price] = quantity
        }
        
        lastUpdateId = snapshot.lastUpdateId
        ready = true
        
        // Process buffered updates
        buffer.filter { it.lastUpdateId > lastUpdateId }.forEach { applyDiff(it) }
        buffer.clear()
        
        updateMetrics()
    }
    
    fun applyDiff(update: DepthUpdate) {
        if (!ready) {
            buffer.add(update)
            return
        }
        
        if (update.lastUpdateId <= lastUpdateId) return
        if (update.firstUpdateId > lastUpdateId + 1) {
            // Gap detected, need resync
            ready = false
            return
        }
        
        lastUpdateId = update.lastUpdateId
        
        // Update bids
        update.bids.forEach { bid ->
            val price = bid[0].toDouble()
            val quantity = bid[1].toDouble()
            if (quantity == 0.0) bids.remove(price) else bids[price] = quantity
        }
        
        // Update asks
        update.asks.forEach { ask ->
            val price = ask[0].toDouble()
            val quantity = ask[1].toDouble()
            if (quantity == 0.0) asks.remove(price) else asks[price] = quantity
        }
        
        updateMetrics()
    }
    
    private fun updateMetrics() {
        _bestBid.value = bids.keys.maxOrNull() ?: 0.0
        _bestAsk.value = asks.keys.minOrNull() ?: Double.MAX_VALUE
        
        val bb = _bestBid.value
        val ba = _bestAsk.value
        
        if (bb > 0 && ba < Double.MAX_VALUE) {
            val mid = (bb + ba) / 2
            val band = mid * 0.01
            
            var bidNotional = 0.0
            var askNotional = 0.0
            
            bids.forEach { (price, quantity) ->
                if (price >= mid - band) bidNotional += price * quantity
            }
            
            asks.forEach { (price, quantity) ->
                if (price <= mid + band) askNotional += price * quantity
            }
            
            val total = bidNotional + askNotional
            _imbalance.value = if (total > 0) (bidNotional - askNotional) / total else 0.0
        }
    }
    
    fun getSpread(): Double {
        val bb = _bestBid.value
        val ba = _bestAsk.value
        return if (bb > 0 && ba < Double.MAX_VALUE) ba - bb else 0.0
    }
    
    fun getWallCount(thresholdPercentile: Int = 90): Int {
        val allNotionals = mutableListOf<Double>()
        
        bids.forEach { (price, quantity) ->
            allNotionals.add(price * quantity)
        }
        
        asks.forEach { (price, quantity) ->
            allNotionals.add(price * quantity)
        }
        
        if (allNotionals.isEmpty()) return 0
        
        allNotionals.sort()
        val thresholdIndex = (allNotionals.size * thresholdPercentile / 100.0).toInt()
        val threshold = allNotionals[minOf(thresholdIndex, allNotionals.size - 1)]
        
        var wallCount = 0
        bids.forEach { (price, quantity) ->
            if (price * quantity >= threshold) wallCount++
        }
        asks.forEach { (price, quantity) ->
            if (price * quantity >= threshold) wallCount++
        }
        
        return wallCount
    }
    
    fun clear() {
        bids.clear()
        asks.clear()
        lastUpdateId = 0
        buffer.clear()
        ready = false
        _bestBid.value = 0.0
        _bestAsk.value = Double.MAX_VALUE
        _imbalance.value = 0.0
    }
}