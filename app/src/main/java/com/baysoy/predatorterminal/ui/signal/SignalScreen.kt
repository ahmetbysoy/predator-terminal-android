package com.baysoy.predatorterminal.ui.signal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Signal(
    val symbol: String,
    val action: String, // "buy", "sell", "neutral"
    val score: Int,
    val reason: String,
    val time: String,
    val imbalance: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignalScreen() {
    val signals = remember { mutableStateListOf<Signal>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050508))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "SİNYAL AKIŞI",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF71717A),
                    letterSpacing = 2.sp
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        if (signals.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Sinyal bekleniyor… Motor 5 sn'de bir tarar.",
                    color = Color(0xFF71717A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(signals) { signal ->
                    SignalCard(signal)
                }
            }
        }
    }
}

@Composable
fun SignalCard(signal: Signal) {
    Surface(
        shape = RoundedCornerShape(15.dp),
        color = Color(0xFF0D0E16).copy(alpha = 0.86f),
        border = ButtonDefaults.outlinedButtonBorder,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = signal.symbol,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                    Text(
                        text = signal.time,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF71717A)
                    )
                }
                Surface(
                    shape = RoundedCornerShape(7.dp),
                    color = when (signal.action) {
                        "buy" -> Color(0xFF00F59B)
                        "sell" -> Color(0xFFFF3358)
                        else -> Color(0xFF71717A)
                    }
                ) {
                    Text(
                        text = signal.action.uppercase(),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = when (signal.action) {
                            "buy" -> Color(0xFF00140C)
                            "sell" -> Color.White
                            else -> Color.White
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = signal.score / 10f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Color(0xFF7C3AED),
                trackColor = Color(0x14FFFFFF),
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = signal.reason,
                fontSize = 12.sp,
                color = Color(0xFF71717A),
                lineHeight = 17.sp
            )
        }
    }
}