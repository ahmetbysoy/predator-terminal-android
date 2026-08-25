package com.baysoy.predatorterminal.domain.repository

import com.baysoy.predatorterminal.data.api.RetrofitClient
import com.baysoy.predatorterminal.data.model.*
import com.baysoy.predatorterminal.data.websocket.WebSocketClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BinanceRepository {
    
    private val api = RetrofitClient.binanceApi
    private val webSocketClient = WebSocketClient()
    
    suspend fun getKlines(symbol: String, interval: String): List<Kline> {
        val response = api.getKlines(symbol, interval)
        return response.map { kline ->
            Kline(
                time = (kline[0] as Double).toLong() / 1000,
                open = (kline[1] as String).toDouble(),
                high = (kline[2] as String).toDouble(),
                low = (kline[3] as String).toDouble(),
                close = (kline[4] as String).toDouble(),
                volume = (kline[5] as String).toDouble()
            )
        }
    }
    
    suspend fun get24hTicker(symbol: String): Ticker24h {
        return api.get24hTicker(symbol)
    }
    
    suspend fun getDepth(symbol: String): OrderBookSnapshot {
        return api.getDepth(symbol)
    }
    
    suspend fun getExchangeInfo(): ExchangeInfo {
        return api.getExchangeInfo()
    }
    
    suspend fun getTradingSymbols(): List<String> {
        val info = getExchangeInfo()
        return info.symbols
            .filter { it.status == "TRADING" && it.quoteAsset == "USDT" }
            .map { it.symbol }
    }
    
    suspend fun getSymbolPrecision(symbol: String): Pair<Int, Double> {
        val info = getExchangeInfo()
        val symbolInfo = info.symbols.find { it.symbol == symbol }
        val priceFilter = symbolInfo?.filters?.find { it.filterType == "PRICE_FILTER" }
        
        return if (priceFilter != null) {
            val tickSize = priceFilter.tickSize?.toDoubleOrNull() ?: 0.01
            val precision = priceFilter.tickSize?.replace("0+$", "")?.replace("\\.$", "")?.let {
                val dotIndex = it.indexOf('.')
                if (dotIndex == -1) 0 else it.length - dotIndex - 1
            } ?: 2
            Pair(precision, tickSize)
        } else {
            Pair(2, 0.01)
        }
    }
    
    fun connectKline(symbol: String, interval: String): Flow<String> {
        return webSocketClient.connectKline(symbol, interval)
    }
    
    fun connectTrade(symbol: String): Flow<String> {
        return webSocketClient.connectTrade(symbol)
    }
    
    fun connectDepth(symbol: String): Flow<String> {
        return webSocketClient.connectDepth(symbol)
    }
    
    fun disconnect() {
        webSocketClient.disconnect()
    }
}