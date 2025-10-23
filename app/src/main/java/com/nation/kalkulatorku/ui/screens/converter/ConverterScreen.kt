package com.nation.kalkulatorku.ui.screens.converter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nation.kalkulatorku.ui.theme.Black
import com.nation.kalkulatorku.ui.theme.Green
import com.nation.kalkulatorku.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConverterScreen(paddingValues: PaddingValues, vm: ConverterViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Converter", color = White, fontSize = 22.sp, fontWeight = FontWeight.Bold)

        CategoryTabs(vm)

        OutlinedTextField(
            value = vm.state.input,
            onValueChange = vm::onInputChange,
            label = { Text("Input", color = White) },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF121212),
                unfocusedContainerColor = Color(0xFF121212),
                focusedTextColor = White,
                unfocusedTextColor = White,
                focusedIndicatorColor = Green,
                unfocusedIndicatorColor = Green,
            ),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        UnitRow(
            title = "From",
            selected = vm.state.fromUnit,
            options = vm.availableUnits(),
            onSelected = vm::onFromUnitChange
        )
        UnitRow(
            title = "To",
            selected = vm.state.toUnit,
            options = vm.availableUnits(),
            onSelected = vm::onToUnitChange
        )

        ConvertResult(vm.state.result)
    }
}

@Composable
private fun CategoryTabs(vm: ConverterViewModel) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        CategoryTab("Temperature", vm.state.category == ConverterCategory.TEMPERATURE) {
            vm.onCategoryChange(ConverterCategory.TEMPERATURE)
        }
        CategoryTab("Length", vm.state.category == ConverterCategory.LENGTH) {
            vm.onCategoryChange(ConverterCategory.LENGTH)
        }
        CategoryTab("Weight", vm.state.category == ConverterCategory.WEIGHT) {
            vm.onCategoryChange(ConverterCategory.WEIGHT)
        }
    }
}

@Composable
private fun CategoryTab(label: String, selected: Boolean, onClick: () -> Unit) {
    val bg by animateColorAsState(if (selected) Green else Color(0xFF1E1E1E), label = "catbg")
    val fg by animateColorAsState(if (selected) Black else White, label = "catfg")
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Box(Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
            Text(label, color = fg, fontWeight = FontWeight.SemiBold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UnitRow(title: String, selected: String, options: List<String>, onSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, color = White)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF121212),
                    unfocusedContainerColor = Color(0xFF121212),
                    focusedTextColor = White,
                    unfocusedTextColor = White,
                    focusedIndicatorColor = Green,
                    unfocusedIndicatorColor = Green
                )
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { opt ->
                    DropdownMenuItem(
                        text = { Text(opt) },
                        onClick = {
                            onSelected(opt)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ConvertResult(result: String) {
    Column(Modifier.fillMaxWidth()) {
        Text("Result", color = White)
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Green)
        ) {
            Box(Modifier.fillMaxWidth().padding(16.dp)) {
                Text(result.ifEmpty { "-" }, color = Black, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


