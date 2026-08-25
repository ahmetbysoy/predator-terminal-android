package com.baysoy.predatorterminal.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.baysoy.predatorterminal.data.model.Kline
import com.baysoy.predatorterminal.data.model.Signal
import com.baysoy.predatorterminal.domain.engine.DepthManager
import com.baysoy.predatorterminal.domain.engine.SignalEngine
import com.baysoy.predatorterminal.domain.repository.BinanceRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ChartViewModel : ViewModel() {
    
    private val repository = BinanceRepository()
    private val signalEngine = SignalEngine()
    private val depthManager = DepthManager()
    
    private val _currentSymbol = MutableStateFlow("BTCUSDT")
    val currentSymbol: StateFlow<String> = _currentSymbol
    
    private val _currentInterval = MutableStateFlow("1m")
    val currentInterval: StateFlow<String> = _currentInterval
    
    private val _currentPrice = MutableStateFlow("—")
    val currentPrice: StateFlow<String> = _currentPrice
    
    private val _priceChange = MutableStateFlow("0.00%")
    val priceChange: StateFlow<String> = _priceChange
    
    private val _isPositive = MutableStateFlow(true)
    val isPositive: StateFlow<Boolean> = _isPositive
    
    private val _klines = MutableStateFlow<List<Kline>>(emptyList())
    val klines: StateFlow<List<Kline>> = _klines
    
    private val _signals = MutableStateFlow<List<Signal>>(emptyList())
    val signals: StateFlow<List<Signal>> = _signals
    
    private val _volume = MutableStateFlow("—")
    val volume: StateFlow<String> = _volume
    
    private val _spread = MutableStateFlow("—")
    val spread: StateFlow<String> = _spread
    
    private val _imbalance = MutableStateFlow("—")
    val imbalance: StateFlow<String> = _imbalance
    
    private val _walls = MutableStateFlow("—")
    val walls: StateFlow<String> = _walls
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error
    
    init {
        loadSymbol(_currentSymbol.value)
        startSignalEngine()
    }
    
    fun loadSymbol(symbol: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                _currentSymbol.value = symbol
                
                // Load klines
                val klines = repository.getKlines(symbol, _currentInterval.value)
                _klines.value = klines
                
                // Load 24h ticker
                val ticker = repository.get24hTicker(symbol)
                _currentPrice.value = ticker.lastPrice
                val changePercent = ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
                _priceChange.value = "${if (changePercent >= 0) "+" else ""}${ticker.priceChangePercent}%"
                _isPositive.value = changePercent >= 0
                _volume.value = formatNotional(ticker.quoteVolume.toDoubleOrNull() ?: 0.0)
                
                // Load depth
                val depth = repository.getDepth(symbol)
                depthManager.applySnapshot(depth)
                
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun switchInterval(interval: String) {
        _currentInterval.value = interval
        loadSymbol(_currentSymbol.value)
    }
    
    private fun startSignalEngine() {
        viewModelScope.launch {
            while (true) {
                delay(5000) // Run every 5 seconds
                
                val imbalance = depthManager.imbalance.value
                val signal = signalEngine.evaluate(_klines.value, imbalance)
                
                if (signal != null && signal.action != "neutral") {
                    val newSignal = signal.copy(symbol = _currentSymbol.value)
                    _signals.value = listOf(newSignal) + _signals.value.take(19) // Keep last 20
                }
                
                // Update HUD
                _spread.value = formatPrice(depthManager.getSpread())
                _imbalance.value = "${if (imbalance >= 0) "+" else ""}${(imbalance * 100).toInt()}%"
                _walls.value = depthManager.getWallCount().toString()
            }
        }
    }
    
    private fun formatPrice(price: Double): String {
        return String.format("%.2f", price)
    }
    
    private fun formatNotional(value: Double): String {
        return when {
            value >= 1e9 -> "$${String.format("%.1f", value / 1e9)}B"
            value >= 1e6 -> "$${String.format("%.1f", value / 1e6)}M"
            value >= 1e3 -> "$${(value / 1e3).toInt()}K"
            else -> "$${value.toInt()}"
        }
    }
    
    override fun onCleared() {
        super.onCleared()
        repository.disconnect()
    }
}