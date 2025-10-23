package com.nation.kalkulatorku

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nation.kalkulatorku.ui.screens.calculator.CalculatorScreen
import com.nation.kalkulatorku.ui.screens.converter.ConverterScreen
import com.nation.kalkulatorku.ui.theme.KalkulatorkuTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KalkulatorkuTheme(darkTheme = true, dynamicColor = false) {
                AppRoot()
            }
        }
    }
}

private enum class AppTab { CALCULATOR, CONVERTER }

@Composable
private fun AppRoot() {
    var selectedTab by remember { mutableStateOf(AppTab.CALCULATOR) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        topBar = {
            NavigationBar(containerColor = Color.Black) {
                NavigationBarItem(
                    selected = selectedTab == AppTab.CALCULATOR,
                    onClick = { selectedTab = AppTab.CALCULATOR },
                    icon = { Icon(Icons.Filled.Calculate, contentDescription = "Calculator") }
                )
                NavigationBarItem(
                    selected = selectedTab == AppTab.CONVERTER,
                    onClick = { selectedTab = AppTab.CONVERTER },
                    icon = { Icon(Icons.Filled.SwapHoriz, contentDescription = "Converter") }
                )
            }
        },
        bottomBar = {}
    ) { paddingValues ->
        when (selectedTab) {
            AppTab.CALCULATOR -> CalculatorScreen(paddingValues)
            AppTab.CONVERTER -> ConverterScreen(paddingValues)
        }
    }
}