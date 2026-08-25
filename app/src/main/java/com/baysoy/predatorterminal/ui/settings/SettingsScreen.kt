package com.baysoy.predatorterminal.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var soundEnabled by remember { mutableStateOf(true) }
    var hapticEnabled by remember { mutableStateOf(true) }
    var logScaleEnabled by remember { mutableStateOf(false) }
    var ladderEnabled by remember { mutableStateOf(true) }
    var wallThreshold by remember { mutableStateOf(90f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050508))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "TERMİNAL AYARLARI",
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

        Surface(
            shape = RoundedCornerShape(15.dp),
            color = Color(0xFF0D0E16).copy(alpha = 0.86f),
            border = ButtonDefaults.outlinedButtonBorder,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 14.dp)
            ) {
                SettingToggle(
                    title = "Ses Efektleri",
                    checked = soundEnabled,
                    onCheckedChange = { soundEnabled = it }
                )
                SettingToggle(
                    title = "Haptic Titreşim",
                    checked = hapticEnabled,
                    onCheckedChange = { hapticEnabled = it }
                )
                SettingToggle(
                    title = "Logaritmik Fiyat Skalası",
                    checked = logScaleEnabled,
                    onCheckedChange = { logScaleEnabled = it }
                )
                SettingToggle(
                    title = "DOM Ladder Şeridi",
                    checked = ladderEnabled,
                    onCheckedChange = { ladderEnabled = it }
                )
                SettingSlider(
                    title = "Duvar Eşiği",
                    value = wallThreshold,
                    onValueChange = { wallThreshold = it },
                    valueRange = 80f..99f,
                    valueText = "P${wallThreshold.toInt()}"
                )
            }
        }
    }
}

@Composable
fun SettingToggle(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.White
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF7C3AED),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF27272A)
            )
        )
    }
    if (title != "Duvar Eşiği") {
        Divider(
            color = Color(0x0DFFFFFF),
            thickness = 1.dp
        )
    }
}

@Composable
fun SettingSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    valueText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 13.sp,
            color = Color.White
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Slider(
                value = value,
                onValueChange = onValueChange,
                valueRange = valueRange,
                modifier = Modifier.width(116.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF7C3AED),
                    activeTrackColor = Color(0xFF7C3AED)
                )
            )
            Text(
                text = valueText,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC4B5FD),
                modifier = Modifier.width(30.dp)
            )
        }
    }
}