package com.baysoy.predatorterminal.data.api

import com.baysoy.predatorterminal.data.model.*
import retrofit2.http.GET
import retrofit2.http.Query

interface BinanceApi {
    
    @GET("api/v3/klines")
    suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("limit") limit: Int = 500
    ): List<List<Any>>
    
    @GET("api/v3/ticker/24hr")
    suspend fun get24hTicker(
        @Query("symbol") symbol: String
    ): Ticker24h
    
    @GET("api/v3/depth")
    suspend fun getDepth(
        @Query("symbol") symbol: String,
        @Query("limit") limit: Int = 5000
    ): OrderBookSnapshot
    
    @GET("api/v3/exchangeInfo")
    suspend fun getExchangeInfo(): ExchangeInfo
}