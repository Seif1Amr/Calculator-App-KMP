import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.isEmpty
import androidx.compose.ui.unit.dp
import androidx.glance.appwidget.compose
import androidx.wear.compose.foundation.weight
import kotlin.text.dropLast
import kotlin.text.isNotEmpty
import kotlin.text.toDoubleOrNull

@androidx.compose.runtime.Composable
fun App() {
    MaterialTheme {
        var display by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("0") }
        var currentInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
        var previousInput by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
        var currentOperator by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

        fun onNumberClick(number: String) {
            if (display == "0" && number != ".") {
                display = number
            } else if (display.contains(".") && number == ".") {
                // Do nothing if "." is already present
            } else {
                display += number
            }
            currentInput += number
        }

        fun onOperatorClick(operator: String) {
            if (currentInput.isNotEmpty()) {
                if (previousInput.isNotEmpty() && currentOperator != null) {
                    // Calculate previous operation before setting new one
                    val result = performCalculation(previousInput, currentInput, currentOperator)
                    display = result.toString()
                    previousInput = result.toString()
                } else {
                    previousInput = currentInput
                }
                currentInput = ""
                currentOperator = operator
            } else if (previousInput.isNotEmpty()) {
                // Allows changing operator if no new number has been input
                currentOperator = operator
            }
        }

        fun onEqualsClick() {
            if (currentInput.isNotEmpty() && previousInput.isNotEmpty() && currentOperator != null) {
                val result = performCalculation(previousInput, currentInput, currentOperator)
                display = result.toString()
                previousInput = result.toString() // Store result for chained calculations
                currentInput = "" // Ready for new input, but display shows the result
                currentOperator = null
            }
        }

        fun onClearClick() {
            display = "0"
            currentInput = ""
            previousInput = ""
            currentOperator = null
        }

        fun onDeleteClick() {
            if (currentInput.isNotEmpty()) {
                currentInput = currentInput.dropLast(1)
                display =
                    if (currentInput.isEmpty() && previousInput.isNotEmpty() && currentOperator != null) {
                        // If deleting makes current input empty, show previous input or 0
                        previousInput
                    } else if (currentInput.isEmpty()) {
                        "0"
                    } else {
                        currentInput
                    }
            } else if (previousInput.isNotEmpty() && currentOperator == null) {
                // If current input is empty and no operator, allow deleting from previous result shown on display
                previousInput = previousInput.dropLast(1)
                display = if (previousInput.isEmpty()) "0" else previousInput
            }
        }


        androidx.compose.foundation.layout.Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = display,
                style = MaterialTheme.typography.h3,
                modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp).align(Alignment.End)
            )

            val buttonModifier = Modifier.fillMaxWidth().weight(1f)

            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton(
                    "C",
                    buttonModifier,
                    MaterialTheme.colors.secondary
                ) { onClearClick() }
                CalculatorButton(
                    "DEL",
                    buttonModifier,
                    MaterialTheme.colors.secondary
                ) { onDeleteClick() }
                CalculatorButton(
                    "%",
                    buttonModifier,
                    MaterialTheme.colors.primaryVariant
                ) { onOperatorClick("%") }
                CalculatorButton(
                    "/",
                    buttonModifier,
                    MaterialTheme.colors.primaryVariant
                ) { onOperatorClick("/") }
            }
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("7", buttonModifier) { onNumberClick("7") }
                CalculatorButton("8", buttonModifier) { onNumberClick("8") }
                CalculatorButton("9", buttonModifier) { onNumberClick("9") }
                CalculatorButton(
                    "*",
                    buttonModifier,
                    MaterialTheme.colors.primaryVariant
                ) { onOperatorClick("*") }
            }
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("4", buttonModifier) { onNumberClick("4") }
                CalculatorButton("5", buttonModifier) { onNumberClick("5") }
                CalculatorButton("6", buttonModifier) { onNumberClick("6") }
                CalculatorButton(
                    "-",
                    buttonModifier,
                    MaterialTheme.colors.primaryVariant
                ) { onOperatorClick("-") }
            }
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("1", buttonModifier) { onNumberClick("1") }
                CalculatorButton("2", buttonModifier) { onNumberClick("2") }
                CalculatorButton("3", buttonModifier) { onNumberClick("3") }
                CalculatorButton(
                    "+",
                    buttonModifier,
                    MaterialTheme.colors.primaryVariant
                ) { onOperatorClick("+") }
            }
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                CalculatorButton("0", Modifier.weight(2f)) { onNumberClick("0") }
                CalculatorButton(".", buttonModifier) { onNumberClick(".") }
                CalculatorButton(
                    "=",
                    buttonModifier,
                    MaterialTheme.colors.primary
                ) { onEqualsClick() }
            }
        }
    }
}

@androidx.compose.runtime.Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: androidx.compose.ui.graphics.Color = MaterialTheme.colors.surface,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.aspectRatio(1.5f)
            .padding(4.dp), // Adjusted aspect ratio for better look
        colors = androidx.compose.material.ButtonDefaults.buttonColors(backgroundColor = backgroundColor)
    ) {
        Text(text, style = MaterialTheme.typography.h6) // Slightly smaller text for button
    }
}

fun performCalculation(val1: String, val2: String, operator: String?): Double {
    val num1 = val1.toDoubleOrNull() ?: 0.0
    val num2 = val2.toDoubleOrNull() ?: 0.0
    return when (operator) {
        "+" -> num1 + num2
        "-" -> num1 - num2
        "*" -> num1 * num2
        "/" -> if (num2 != 0.0) num1 / num2 else Double.NaN // Handle division by zero
        "%" -> if (num2 != 0.0) num1 % num2 else Double.NaN // Handle modulo by zero
        else -> num2 // Should not happen with current logic, effectively makes current num the result
    }
}