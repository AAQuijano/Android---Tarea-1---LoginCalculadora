package com.quijano.tarea_1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calculadora() {
    var input by remember { mutableStateOf("0") }
    var firstOperand by remember { mutableStateOf(0.0) }
    var operation by remember { mutableStateOf<Operation?>(null) }
    var shouldResetInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Mostrar operación actual (opcional)
        if (operation != null) {
            Text(
                text = "$firstOperand ${operation!!.symbol()}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        // Pantalla
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 4.dp
        ) {
            Text(
                text = input,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.End,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        fun handleNumber(number: String) {
            input = when {
                shouldResetInput -> number
                input == "0" -> number
                else -> input + number
            }
            shouldResetInput = false
        }

        fun handleOperation(op: Operation) {
            firstOperand = input.toDoubleOrNull() ?: 0.0
            operation = op
            shouldResetInput = true
        }

        val operationButtonColor = MaterialTheme.colorScheme.primary
        val functionButtonColor = MaterialTheme.colorScheme.secondary
        val numberButtonColor = MaterialTheme.colorScheme.surfaceVariant

        val buttonHeight = 80.dp
        val normalFontSize = 24.sp
        val operationFontSize = 28.sp

        // Fila 1
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("C", functionButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) {
                input = "0"
                firstOperand = 0.0
                operation = null
            }
            CalcButton("⌫", functionButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) {
                input = input.dropLast(1).ifEmpty { "0" }
            }
            CalcButton("÷", operationButtonColor, Modifier.weight(1f).height(buttonHeight), operationFontSize) {
                handleOperation(Operation.DIVIDE)
            }
            CalcButton("×", operationButtonColor, Modifier.weight(1f).height(buttonHeight), operationFontSize) {
                handleOperation(Operation.MULTIPLY)
            }
        }

        // Fila 2
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("7", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("7") }
            CalcButton("8", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("8") }
            CalcButton("9", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("9") }
            CalcButton("-", operationButtonColor, Modifier.weight(1f).height(buttonHeight), operationFontSize) {
                handleOperation(Operation.SUBTRACT)
            }
        }

        // Fila 3
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("4", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("4") }
            CalcButton("5", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("5") }
            CalcButton("6", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("6") }
            CalcButton("+", operationButtonColor, Modifier.weight(1f).height(buttonHeight), operationFontSize) {
                handleOperation(Operation.ADD)
            }
        }

        // Fila 4
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("1", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("1") }
            CalcButton("2", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("2") }
            CalcButton("3", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) { handleNumber("3") }
            CalcButton("=", operationButtonColor, Modifier.weight(1f).height(buttonHeight), operationFontSize) {
                val secondOperand = input.toDoubleOrNull() ?: 0.0
                val result = when (operation) {
                    Operation.ADD -> firstOperand + secondOperand
                    Operation.SUBTRACT -> firstOperand - secondOperand
                    Operation.MULTIPLY -> firstOperand * secondOperand
                    Operation.DIVIDE -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
                    null -> secondOperand
                }
                input = when {
                    result.isNaN() -> "Error"
                    result % 1 == 0.0 -> result.toInt().toString()
                    else -> result.toString()
                }
                operation = null
                shouldResetInput = true
            }
        }

        // Fila 5
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("0", numberButtonColor, Modifier.weight(2f).height(buttonHeight), normalFontSize) { handleNumber("0") }
            CalcButton(".", numberButtonColor, Modifier.weight(1f).height(buttonHeight), normalFontSize) {
                if (!input.contains(".")) handleNumber(".")
            }
        }
    }
}

@Composable
fun CalcButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class Operation {
    ADD, SUBTRACT, MULTIPLY, DIVIDE;

    fun symbol(): String = when (this) {
        ADD -> "+"
        SUBTRACT -> "-"
        MULTIPLY -> "×"
        DIVIDE -> "÷"
    }
}

/*
package com.quijano.tarea_1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calculadora() {
    var input by remember { mutableStateOf("0") }
    var firstOperand by remember { mutableDoubleStateOf(0.0) }
    var operation by remember { mutableStateOf<Operation?>(null) }
    var shouldResetInput by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Pantalla mejorada
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            shape = RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 4.dp
        ) {
            Text(
                text = input,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.End,
                fontSize = 48.sp,
                fontWeight = FontWeight.Light,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Función auxiliar para manejar números
        fun handleNumber(number: String) {
            input = when {
                shouldResetInput -> number
                input == "0" -> number
                else -> input + number
            }
            shouldResetInput = false
        }

        // Función auxiliar para manejar operaciones
        fun handleOperation(op: Operation) {
            firstOperand = input.toDouble()
            operation = op
            shouldResetInput = true
        }

        // Configuración de colores y tamaños
        val operationButtonColor = MaterialTheme.colorScheme.primary
        val functionButtonColor = MaterialTheme.colorScheme.secondary
        val numberButtonColor = MaterialTheme.colorScheme.surfaceVariant

        val buttonHeight = 80.dp
        val normalFontSize = 24.sp
        val operationFontSize = 28.sp

        // Filas de botones
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton(
                text = "C",
                color = functionButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = normalFontSize
            ) {
                input = "0"
                firstOperand = 0.0
                operation = null
            }
            CalcButton(
                text = "⌫",
                color = functionButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = normalFontSize
            ) {
                input = input.dropLast(1).ifEmpty { "0" }
            }
            CalcButton(
                text = "÷",
                color = operationButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = operationFontSize
            ) {
                handleOperation(Operation.DIVIDE)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("7", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("7") }
            CalcButton("8", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("8") }
            CalcButton("9", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("9") }
            CalcButton(
                text = "×",
                color = operationButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = operationFontSize
            ) {
                handleOperation(Operation.MULTIPLY)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("4", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("4") }
            CalcButton("5", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("5") }
            CalcButton("6", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("6") }
            CalcButton(
                text = "-",
                color = operationButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = operationFontSize
            ) {
                handleOperation(Operation.SUBTRACT)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("1", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("1") }
            CalcButton("2", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("2") }
            CalcButton("3", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) { handleNumber("3") }
            CalcButton(
                text = "+",
                color = operationButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = operationFontSize
            ) {
                handleOperation(Operation.ADD)
            }
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalcButton("0", numberButtonColor, Modifier.fillMaxWidth(0.49f).height(buttonHeight), normalFontSize) { handleNumber("0") }
            CalcButton(".", numberButtonColor, Modifier.fillMaxWidth(0.24f).height(buttonHeight), normalFontSize) {
                if (!input.contains(".")) handleNumber(".")
            }
            CalcButton(
                text = "=",
                color = operationButtonColor,
                modifier = Modifier.fillMaxWidth(0.24f).height(buttonHeight),
                fontSize = operationFontSize
            ) {
                if (operation != null) {
                    val secondOperand = input.toDouble()
                    val result = when (operation) {
                        Operation.ADD -> firstOperand + secondOperand
                        Operation.SUBTRACT -> firstOperand - secondOperand
                        Operation.MULTIPLY -> firstOperand * secondOperand
                        Operation.DIVIDE -> if (secondOperand != 0.0) firstOperand / secondOperand else Double.NaN
                        null -> secondOperand
                    }
                    input = when {
                        result.isNaN() -> "Error"
                        result % 1 == 0.0 -> result.toInt().toString()
                        else -> result.toString()
                    }
                    operation = null
                    shouldResetInput = true
                }
            }
        }
    }
}

@Composable
fun CalcButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 24.sp,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 8.dp
        )
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

enum class Operation { ADD, SUBTRACT, MULTIPLY, DIVIDE }

*/