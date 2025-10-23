package com.nation.kalkulatorku.ui.screens.converter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

enum class ConverterCategory { TEMPERATURE, LENGTH, WEIGHT }

data class ConverterState(
    val category: ConverterCategory = ConverterCategory.TEMPERATURE,
    val input: String = "",
    val fromUnit: String = "°C",
    val toUnit: String = "°F",
    val result: String = ""
)

class ConverterViewModel : ViewModel() {
    var state by mutableStateOf(ConverterState())
        private set

    private val units = mapOf(
        ConverterCategory.TEMPERATURE to listOf("°C", "°F"),
        ConverterCategory.LENGTH to listOf("cm", "m", "km"),
        ConverterCategory.WEIGHT to listOf("g", "kg", "ton")
    )

    fun onCategoryChange(category: ConverterCategory) {
        val opts = units[category]!!
        state = state.copy(
            category = category,
            fromUnit = opts.first(),
            toUnit = opts.last(),
            result = compute(state.input, opts.first(), opts.last(), category)
        )
    }

    fun onInputChange(input: String) {
        val sanitized = input.filter { it.isDigit() || it == '.' }
        state = state.copy(
            input = sanitized,
            result = compute(sanitized, state.fromUnit, state.toUnit, state.category)
        )
    }

    fun onFromUnitChange(unit: String) {
        state = state.copy(
            fromUnit = unit,
            result = compute(state.input, unit, state.toUnit, state.category)
        )
    }

    fun onToUnitChange(unit: String) {
        state = state.copy(
            toUnit = unit,
            result = compute(state.input, state.fromUnit, unit, state.category)
        )
    }

    fun availableUnits(): List<String> = units[state.category]!!

    private fun compute(input: String, from: String, to: String, category: ConverterCategory): String {
        val value = input.toDoubleOrNull() ?: return ""
        val result = when (category) {
            ConverterCategory.TEMPERATURE -> temp(value, from, to)
            ConverterCategory.LENGTH -> length(value, from, to)
            ConverterCategory.WEIGHT -> weight(value, from, to)
        }
        return trim(result)
    }

    private fun temp(v: Double, from: String, to: String): Double {
        return when (from to to) {
            "°C" to "°F" -> v * 9 / 5 + 32
            "°F" to "°C" -> (v - 32) * 5 / 9
            else -> v
        }
    }

    private fun length(v: Double, from: String, to: String): Double {
        val meters = when (from) {
            "cm" -> v / 100.0
            "m" -> v
            "km" -> v * 1000.0
            else -> v
        }
        return when (to) {
            "cm" -> meters * 100.0
            "m" -> meters
            "km" -> meters / 1000.0
            else -> meters
        }
    }

    private fun weight(v: Double, from: String, to: String): Double {
        val grams = when (from) {
            "g" -> v
            "kg" -> v * 1000.0
            "ton" -> v * 1_000_000.0
            else -> v
        }
        return when (to) {
            "g" -> grams
            "kg" -> grams / 1000.0
            "ton" -> grams / 1_000_000.0
            else -> grams
        }
    }

    private fun trim(value: Double): String {
        val longVal = value.toLong()
        return if (value == longVal.toDouble()) longVal.toString() else value.toString()
    }
}


