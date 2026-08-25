package com.baysoy.predatorterminal.ui.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.tradingview.lightweightcharts.api.chartApis
import com.tradingview.lightweightcharts.api.options.models.*
import com.tradingview.lightweightcharts.api.series.models.*
import com.tradingview.lightweightcharts.api.series.enums.SeriesType
import com.tradingview.lightweightcharts.view.ChartsView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen() {
    var currentSymbol by remember { mutableStateOf("BTCUSDT") }
    var currentPrice by remember { mutableStateOf("—") }
    var priceChange by remember { mutableStateOf("0.00%") }
    var isPositive by remember { mutableStateOf(true) }
    var selectedTimeframe by remember { mutableStateOf("1m") }
    val timeframes = listOf("1m", "5m", "15m", "1h")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050508))
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = currentSymbol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFF7C3AED).copy(alpha = 0.3f),
                        border = ButtonDefaults.outlinedButtonBorder
                    ) {
                        Text(
                            text = "SPOT",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFC4B5FD)
                        )
                    }
                }
            },
            actions = {
                IconButton(onClick = { /* Toggle star */ }) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "Star",
                        tint = Color(0xFFFACC15)
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = currentPrice,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isPositive) Color(0xFF00F59B).copy(alpha = 0.14f) 
                               else Color(0xFFFF3358).copy(alpha = 0.14f)
                    ) {
                        Text(
                            text = priceChange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isPositive) Color(0xFF00F59B) else Color(0xFFFF3358)
                        )
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF0D0E16).copy(alpha = 0.86f)
            )
        )

        // Timeframe selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF12131C).copy(alpha = 0.86f),
                border = ButtonDefaults.outlinedButtonBorder
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    timeframes.forEach { tf ->
                        Surface(
                            shape = RoundedCornerShape(9.dp),
                            color = if (selectedTimeframe == tf) Color(0xFF7C3AED) 
                                   else Color.Transparent,
                            onClick = { selectedTimeframe = tf }
                        ) {
                            Text(
                                text = tf,
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selectedTimeframe == tf) Color.White 
                                       else Color(0xFF71717A)
                            )
                        }
                    }
                }
            }
        }

        // Chart
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            AndroidView(
                factory = { context ->
                    ChartsView(context).apply {
                        api.applyOptions {
                            layout = layoutOptions {
                                background = Color.LTGRAY.toIntColor()
                                textColor = Color.BLACK.toIntColor()
                            }
                            grid = gridOptions {
                                vertLines = GridLineOptions(color = Color(0x0DFFFFFF))
                                horzLines = GridLineOptions(color = Color(0x0DFFFFFF))
                            }
                            rightPriceScale = PriceScaleOptions(
                                borderColor = Color(0x14FFFFFF)
                            )
                            timeScale = TimeScaleOptions(
                                borderColor = Color(0x14FFFFFF),
                                timeVisible = true,
                                secondsVisible = false
                            )
                            crosshair = CrosshairOptions(
                                vertLine = CrosshairLineOptions(
                                    color = Color(0x737C3AED),
                                    width = 1,
                                    style = LineStyle.DASHED
                                ),
                                horzLine = CrosshairLineOptions(
                                    color = Color(0x737C3AED),
                                    width = 1,
                                    style = LineStyle.DASHED
                                )
                            )
                        }
                        api.addCandlestickSeries(
                            options = CandlestickSeriesOptions(
                                upColor = Color(0xFF00F59B),
                                downColor = Color(0xFFFF3358),
                                borderUpColor = Color(0xFF00F59B),
                                borderDownColor = Color(0xFFFF3358),
                                wickUpColor = Color(0xFF00F59B),
                                wickDownColor = Color(0xFFFF3358)
                            ),
                            onSeriesCreated = { series ->
                                // Load data here
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }

        // Footer info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .background(
                    Color(0xFF0D0E16).copy(alpha = 0.86f),
                    RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 10.dp, vertical = 7.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            FooterInfoItem("VOL", "—")
            FooterInfoItem("SPREAD", "—")
            FooterInfoItem("IMB", "—")
            FooterInfoItem("DUVAR", "—")
        }
    }
}

@Composable
fun FooterInfoItem(label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            color = Color(0xFF71717A)
        )
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}