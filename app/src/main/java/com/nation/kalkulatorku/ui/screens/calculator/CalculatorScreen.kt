package com.nation.kalkulatorku.ui.screens.calculator

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nation.kalkulatorku.ui.theme.Black
import com.nation.kalkulatorku.ui.theme.White
import com.nation.kalkulatorku.ui.theme.Grey600
import com.nation.kalkulatorku.ui.theme.Orange

@Composable
fun CalculatorScreen(paddingValues: PaddingValues, vm: CalculatorViewModel = viewModel()) {
    var menuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("Kalkulator", color = White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Box {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Filled.MoreVert, contentDescription = "More", tint = White)
                }
                DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                    DropdownMenuItem(text = { Text("History") }, onClick = { menuExpanded = false; })
                }
            }
        }
        DisplayArea(text = vm.state.display, error = vm.state.error)
        Keypad(
            onDigit = vm::onDigit,
            onDot = vm::onDot,
            onOperator = { op -> vm.onOperator(op) },
            onEquals = vm::onEquals,
            onClear = vm::clear,
            onBackspace = vm::backspace
        )
    }
}

@Composable
private fun DisplayArea(text: String, error: String?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.End
    ) {
        if (error != null) {
            Text(
                text = error,
                color = Color.Red,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(Modifier.height(4.dp))
        }
        Text(
            text = text,
            color = White,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun Keypad(
    onDigit: (Char) -> Unit,
    onDot: () -> Unit,
    onOperator: (Operator) -> Unit,
    onEquals: () -> Unit,
    onClear: () -> Unit,
    onBackspace: () -> Unit
) {
    val rows = listOf(
        listOf(Key("AC", isOperator = true) { onClear() }, Key("⌫", isOperator = true) { onBackspace() }, Key("%", isOperator = true) { onOperator(Operator.PERCENT) }, Key("÷", isOperator = true) { onOperator(Operator.DIVIDE) }),
        listOf(Key("7") { onDigit('7') }, Key("8") { onDigit('8') }, Key("9") { onDigit('9') }, Key("×", isOperator = true) { onOperator(Operator.MULTIPLY) }),
        listOf(Key("4") { onDigit('4') }, Key("5") { onDigit('5') }, Key("6") { onDigit('6') }, Key("-", isOperator = true) { onOperator(Operator.SUBTRACT) }),
        listOf(Key("1") { onDigit('1') }, Key("2") { onDigit('2') }, Key("3") { onDigit('3') }, Key("+", isOperator = true) { onOperator(Operator.ADD) }),
        listOf(Key("0") { onDigit('0') }, Key(",") { onDot() }, Key("=", isOperator = true, isAccent = true) { onEquals() })
    )

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { key ->
                    val weight = if (key.label == "0" && row.size == 3) 2f else 1f
                    CalculatorButton(
                        label = key.label,
                        isOperator = key.isOperator,
                        modifier = Modifier.weight(weight)
                    ) { key.onClick() }
                }
            }
        }
    }
}

private data class Key(
    val label: String,
    val isOperator: Boolean = false,
    val isAccent: Boolean = false,
    val onClick: () -> Unit
)

@Composable
private fun CalculatorButton(label: String, isOperator: Boolean, modifier: Modifier = Modifier, isAccent: Boolean = false, onClick: () -> Unit) {
    // Style: numbers dark grey with white text, operators orange with black text, equals accented orange
    val targetBg = when {
        isAccent -> Orange
        isOperator -> Grey600
        else -> Grey600
    }
    val targetText = when {
        isAccent -> Black
        isOperator -> Orange
        else -> White
    }
    val bg by animateColorAsState(targetValue = targetBg, label = "bg")
    val text by animateColorAsState(targetValue = targetText, label = "fg")

    Card(
        modifier = modifier
            .height(64.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(text = label, color = text, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}


