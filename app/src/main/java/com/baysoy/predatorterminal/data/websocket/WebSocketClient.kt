package com.baysoy.predatorterminal.data.websocket

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*
import java.util.concurrent.TimeUnit

class WebSocketClient {
    
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    
    private val client = OkHttpClient.Builder()
        .pingInterval(30, TimeUnit.SECONDS)
        .build()
    
    private var webSocket: WebSocket? = null
    
    fun connectKline(symbol: String, interval: String): Flow<String> = callbackFlow {
        val url = "wss://stream.binance.com:9443/ws/${symbol.lowercase()}@kline_$interval"
        val request = Request.Builder().url(url).build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(text)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                close()
            }
        })
        
        awaitClose {
            webSocket?.close(1000, "Closed")
        }
    }
    
    fun connectTrade(symbol: String): Flow<String> = callbackFlow {
        val url = "wss://stream.binance.com:9443/ws/${symbol.lowercase()}@trade"
        val request = Request.Builder().url(url).build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(text)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                close()
            }
        })
        
        awaitClose {
            webSocket?.close(1000, "Closed")
        }
    }
    
    fun connectDepth(symbol: String): Flow<String> = callbackFlow {
        val url = "wss://stream.binance.com:9443/ws/${symbol.lowercase()}@depth@100ms"
        val request = Request.Builder().url(url).build()
        
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                trySend(text)
            }
            
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                close(t)
            }
            
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                close()
            }
        })
        
        awaitClose {
            webSocket?.close(1000, "Closed")
        }
    }
    
    fun disconnect() {
        webSocket?.close(1000, "Client closed")
        webSocket = null
    }
}