package com.nation.kalkulatorku.ui.screens.calculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

// Calculator operations
enum class Operator { ADD, SUBTRACT, MULTIPLY, DIVIDE, PERCENT }

data class CalculatorState(
    val display: String = "0",
    val error: String? = null,
    val history: List<String> = emptyList()
)

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    private var currentValue: String = "0"
    private var pendingOperator: Operator? = null
    private var accumulator: Double? = null
    private var justEvaluated: Boolean = false

    fun onDigit(digit: Char) {
        if (digit !in '0'..'9') return
        if (justEvaluated) {
            clear()
        }
        if (currentValue == "0") currentValue = digit.toString() else currentValue += digit
        updateDisplay()
    }

    fun onDot() {
        if (justEvaluated) {
            clear()
        }
        if (!currentValue.contains('.')) {
            currentValue += if (currentValue.isEmpty()) "0." else "."
            updateDisplay()
        }
    }

    fun onOperator(op: Operator) {
        if (op == Operator.PERCENT) {
            applyPercent()
            return
        }
        resolvePending()
        pendingOperator = op
        accumulator = currentValue.toDoubleOrNull() ?: 0.0
        currentValue = "0"
        justEvaluated = false
        updateDisplay()
    }

    fun onEquals() {
        val before = accumulator
        val op = pendingOperator
        val rhsVal = currentValue
        resolvePending()
        val resultShown = currentValue
        if (before != null && op != null) {
            val entry = "${trim(before)} ${symbol(op)} $rhsVal = $resultShown"
            state = state.copy(history = (listOf(entry) + state.history).take(20))
        }
        pendingOperator = null
        justEvaluated = true
        updateDisplay()
    }

    fun clear() {
        currentValue = "0"
        pendingOperator = null
        accumulator = null
        justEvaluated = false
        state = CalculatorState(display = currentValue, error = null)
    }

    private fun updateDisplay() {
        state = state.copy(display = currentValue, error = state.error)
    }

    private fun resolvePending() {
        val rhs = currentValue.toDoubleOrNull() ?: return
        val lhs = accumulator
        if (lhs == null) {
            accumulator = rhs
            currentValue = trim(rhs)
            return
        }
        val result = when (pendingOperator) {
            Operator.ADD -> lhs + rhs
            Operator.SUBTRACT -> lhs - rhs
            Operator.MULTIPLY -> lhs * rhs
            Operator.DIVIDE -> {
                if (rhs == 0.0) {
                    state = state.copy(error = "Cannot divide by 0")
                    return
                } else lhs / rhs
            }
            else -> rhs
        }
        accumulator = result
        currentValue = trim(result)
        state = state.copy(error = null)
    }

    private fun applyPercent() {
        val value = currentValue.toDoubleOrNull() ?: return
        val result = value / 100.0
        currentValue = trim(result)
        updateDisplay()
    }

    fun backspace() {
        if (justEvaluated) return
        currentValue = if (currentValue.length <= 1) "0" else currentValue.dropLast(1)
        updateDisplay()
    }

    private fun symbol(op: Operator): String = when (op) {
        Operator.ADD -> "+"
        Operator.SUBTRACT -> "-"
        Operator.MULTIPLY -> "×"
        Operator.DIVIDE -> "÷"
        Operator.PERCENT -> "%"
    }

    private fun trim(value: Double): String {
        val longVal = value.toLong()
        return if (value == longVal.toDouble()) longVal.toString() else value.toString()
    }
}


