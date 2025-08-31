package org.example.project

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button // Changed from material to material3
import androidx.compose.material3.ButtonDefaults // Added for M3 Button colors
import androidx.compose.material3.MaterialTheme // Changed from material to material3
import androidx.compose.material3.Text // Changed from material to material3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color // Keep for Color type
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign

@Composable
fun App() {
    MaterialTheme { // This will now use M3 MaterialTheme
        var display by remember { mutableStateOf("0") }
        var currentInput by remember { mutableStateOf("") }
        var previousInput by remember { mutableStateOf("") }
        var currentOperator by remember { mutableStateOf<String?>(null) }

        fun onNumberClick(number: String) {
            if (display == "0" && number != ".") {
                display = number
            } else if (display.contains(".") && number == ".") {
                // Do nothing if "." is already present
            }
            else {
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
                display = if (currentInput.isEmpty() && previousInput.isNotEmpty() && currentOperator != null) {
                    // If deleting makes current input empty, show previous input or 0
                     previousInput
                } else if (currentInput.isEmpty()){
                    "0"
                }
                else {
                    currentInput
                }
            } else if (previousInput.isNotEmpty() && currentOperator == null) {
                // If current input is empty and no operator, allow deleting from previous result shown on display
                previousInput = previousInput.dropLast(1)
                display = if (previousInput.isEmpty()) "0" else previousInput
            }
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom // Push content to the bottom
        ) {
            Spacer(Modifier.weight(1f)) // Empty space at the top, pushes content down and shrinks if display grows

            Text(
                text = display,
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.End, // Align text to the end (right)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp) // Padding for the display area
            )

            // Spacing between display and first button row
            Spacer(modifier = Modifier.height(16.dp))

            val buttonModifier = Modifier.weight(1f) // Modifier for standard width buttons in a row

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("C", buttonModifier, containerColor = MaterialTheme.colorScheme.secondaryContainer) { onClearClick() }
                CalculatorButton("DEL", buttonModifier, containerColor = MaterialTheme.colorScheme.secondaryContainer) { onDeleteClick() }
                CalculatorButton("%", buttonModifier, containerColor = MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("%") }
                CalculatorButton("/", buttonModifier, containerColor = MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("/") }
            }
            Spacer(modifier = Modifier.height(8.dp)) // Spacing between button rows

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("7", buttonModifier) { onNumberClick("7") }
                CalculatorButton("8", buttonModifier) { onNumberClick("8") }
                CalculatorButton("9", buttonModifier) { onNumberClick("9") }
                CalculatorButton("*", buttonModifier, containerColor = MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("*") }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("4", buttonModifier) { onNumberClick("4") }
                CalculatorButton("5", buttonModifier) { onNumberClick("5") }
                CalculatorButton("6", buttonModifier) { onNumberClick("6") }
                CalculatorButton("-", buttonModifier, containerColor = MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("-") }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("1", buttonModifier) { onNumberClick("1") }
                CalculatorButton("2", buttonModifier) { onNumberClick("2") }
                CalculatorButton("3", buttonModifier) { onNumberClick("3") }
                CalculatorButton("+", buttonModifier, containerColor = MaterialTheme.colorScheme.tertiaryContainer) { onOperatorClick("+") }
            }
            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                CalculatorButton("0", buttonModifier) { onNumberClick("0") } // "0" button now takes 1f weight
                CalculatorButton(".", buttonModifier) { onNumberClick(".") }
                // "=" button now takes 2f weight to fill the remaining space of two "columns"
                // if we maintain 4 distinct button areas in each row.
                // Or, if all three buttons (0, ., =) should be equal width, it also gets buttonModifier (1f).
                // Assuming "0" should be same size as "1", ".", and "=" can also be that same size.
                CalculatorButton("=", buttonModifier, containerColor = MaterialTheme.colorScheme.primaryContainer) { onEqualsClick() }
            }
        }
    }
}

@Composable
fun CalculatorButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1.5f) // Adjust aspect ratio as needed, 1.5f makes it wider than tall
            .padding(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        Text(text, style = MaterialTheme.typography.titleLarge)
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
