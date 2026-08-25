package com.baysoy.predatorterminal.domain.engine

import com.baysoy.predatorterminal.data.model.Kline
import com.baysoy.predatorterminal.data.model.Signal
import java.text.SimpleDateFormat
import java.util.*

class SignalEngine {
    
    fun evaluate(candles: List<Kline>, imbalance: Double): Signal? {
        if (candles.size < 35) return null
        
        val closes = candles.map { it.close }
        val rsi = calculateRSI(closes)
        val macd = calculateMACD(closes)
        
        var score = 0
        val reasons = mutableListOf<String>()
        
        // RSI Analysis
        if (rsi != null) {
            when {
                rsi < 32 -> {
                    score += 3
                    reasons.add("RSI Aşırı Satım (${rsi.toInt()})")
                }
                rsi > 68 -> {
                    score -= 3
                    reasons.add("RSI Aşırı Alım (${rsi.toInt()})")
                }
            }
        }
        
        // MACD Analysis
        if (macd != null) {
            when {
                macd.histogram > 0 && macd.macd > macd.signal -> {
                    score += 2
                    reasons.add("MACD Bull Cross")
                }
                macd.histogram < 0 && macd.macd < macd.signal -> {
                    score -= 2
                    reasons.add("MACD Bear Cross")
                }
            }
        }
        
        // Imbalance Analysis
        when {
            imbalance > 0.25 -> {
                score += 2
                reasons.add("Alıcı Likidite Baskısı (+%${(imbalance * 100).toInt()})")
            }
            imbalance < -0.25 -> {
                score -= 2
                reasons.add("Satıcı Likidite Baskısı (-%${(Math.abs(imbalance) * 100).toInt()})")
            }
        }
        
        val action = when {
            score >= 3 -> "buy"
            score <= -3 -> "sell"
            else -> "neutral"
        }
        
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale("tr", "TR"))
        val currentTime = timeFormat.format(Date())
        
        return Signal(
            symbol = "", // Will be set by caller
            action = action,
            score = minOf(Math.abs(score), 10),
            reason = reasons.joinToString(" • ").ifEmpty { "Nötr piyasa" },
            time = currentTime,
            imbalance = imbalance * 100
        )
    }
    
    private fun calculateRSI(closes: List<Double>, period: Int = 14): Double? {
        if (closes.size < period + 1) return null
        
        var gain = 0.0
        var loss = 0.0
        
        for (i in closes.size - period until closes.size) {
            val change = closes[i] - closes[i - 1]
            if (change >= 0) gain += change else loss -= change
        }
        
        if (loss == 0.0) return 100.0
        
        val avgGain = gain / period
        val avgLoss = loss / period
        val rs = avgGain / avgLoss
        
        return 100.0 - 100.0 / (1.0 + rs)
    }
    
    private fun calculateMACD(closes: List<Double>): MACDResult? {
        if (closes.size < 35) return null
        
        val ema12 = calculateEMA(closes, 12)
        val ema26 = calculateEMA(closes, 26)
        
        val macdLine = ema12.zip(ema26) { a, b -> a - b }
        val signalLine = calculateEMA(macdLine.drop(26), 9)
        
        val macd = macdLine.last()
        val signal = signalLine.last()
        val histogram = macd - signal
        
        return MACDResult(macd, signal, histogram)
    }
    
    private fun calculateEMA(data: List<Double>, period: Int): List<Double> {
        val k = 2.0 / (period + 1)
        val ema = mutableListOf(data[0])
        
        for (i in 1 until data.size) {
            ema.add(data[i] * k + ema[i - 1] * (1 - k))
        }
        
        return ema
    }
    
    data class MACDResult(
        val macd: Double,
        val signal: Double,
        val histogram: Double
    )
}