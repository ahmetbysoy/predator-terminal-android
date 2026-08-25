package com.baysoy.predatorterminal.ui.watchlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class WatchlistItem(
    val symbol: String,
    val price: String,
    val change: String,
    val isPositive: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WatchlistScreen() {
    val watchlist = remember {
        mutableStateListOf(
            WatchlistItem("BTCUSDT", "42,150.00", "+2.35%", true),
            WatchlistItem("ETHUSDT", "2,250.00", "-1.20%", false),
            WatchlistItem("SOLUSDT", "98.50", "+5.67%", true)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050508))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "TAKİP LİSTESİ",
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

        if (watchlist.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(42.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Takip listesi boş. Header'daki ★ ile ekle.",
                    color = Color(0xFF71717A),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 7.dp),
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                items(watchlist) { item ->
                    WatchlistRow(
                        item = item,
                        onRemove = { watchlist.remove(item) },
                        onClick = { /* Navigate to chart */ }
                    )
                }
            }
        }
    }
}

@Composable
fun WatchlistRow(
    item: WatchlistItem,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(13.dp),
        color = Color(0xFF0D0E16).copy(alpha = 0.86f),
        border = ButtonDefaults.outlinedButtonBorder,
        shadowElevation = 6.dp,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 13.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = item.symbol,
                    fontWeight = FontWeight.Black,
                    fontSize = 14.sp,
                    color = Color.White
                )
                Text(
                    text = item.price,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF71717A)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (item.isPositive) Color(0xFF00F59B).copy(alpha = 0.14f)
                           else Color(0xFFFF3358).copy(alpha = 0.14f),
                    border = ButtonDefaults.outlinedButtonBorder
                ) {
                    Text(
                        text = item.change,
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isPositive) Color(0xFF00F59B) else Color(0xFFFF3358)
                    )
                }
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Remove",
                        tint = Color(0xFF52525B),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}