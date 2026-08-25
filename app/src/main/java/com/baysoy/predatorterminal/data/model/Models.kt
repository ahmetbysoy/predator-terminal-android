package com.baysoy.predatorterminal.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Ticker24h(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "priceChange") val priceChange: String,
    @Json(name = "priceChangePercent") val priceChangePercent: String,
    @Json(name = "lastPrice") val lastPrice: String,
    @Json(name = "quoteVolume") val quoteVolume: String
)

@JsonClass(generateAdapter = true)
data class OrderBookSnapshot(
    @Json(name = "lastUpdateId") val lastUpdateId: Long,
    @Json(name = "bids") val bids: List<List<String>>,
    @Json(name = "asks") val asks: List<List<String>>
)

@JsonClass(generateAdapter = true)
data class ExchangeInfo(
    @Json(name = "symbols") val symbols: List<SymbolInfo>
)

@JsonClass(generateAdapter = true)
data class SymbolInfo(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "status") val status: String,
    @Json(name = "baseAsset") val baseAsset: String,
    @Json(name = "quoteAsset") val quoteAsset: String,
    @Json(name = "filters") val filters: List<Filter>
)

@JsonClass(generateAdapter = true)
data class Filter(
    @Json(name = "filterType") val filterType: String,
    @Json(name = "tickSize") val tickSize: String? = null
)

data class Kline(
    val time: Long,
    val open: Double,
    val high: Double,
    val low: Double,
    val close: Double,
    val volume: Double
)

data class DepthUpdate(
    @Json(name = "e") val eventType: String,
    @Json(name = "E") val eventTime: Long,
    @Json(name = "s") val symbol: String,
    @Json(name = "U") val firstUpdateId: Long,
    @Json(name = "u") val lastUpdateId: Long,
    @Json(name = "b") val bids: List<List<String>>,
    @Json(name = "a") val asks: List<List<String>>
)

data class TradeUpdate(
    @Json(name = "e") val eventType: String,
    @Json(name = "E") val eventTime: Long,
    @Json(name = "s") val symbol: String,
    @Json(name = "p") val price: String,
    @Json(name = "q") val quantity: String,
    @Json(name = "T") val tradeTime: Long
)

data class Signal(
    val symbol: String,
    val action: String, // "buy", "sell", "neutral"
    val score: Int,
    val reason: String,
    val time: String,
    val imbalance: Double
)