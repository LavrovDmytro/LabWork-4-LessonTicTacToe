package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class GameScore(
    var xWins: Int = 0,
    var oWins: Int = 0,
    var draws: Int = 0
)

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedSize by remember { mutableStateOf(3) }
    var gameStarted by remember { mutableStateOf(false) }
    var gameScore by remember { mutableStateOf(GameScore()) }
    var showScoreDialog by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(
            text = "Tic Tac Toe",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign = TextAlign.Center
        )

        if (!gameStarted) {
            Text(
                text = "Select Board Size",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (size in 3..5) {
                    Button(
                        onClick = {
                            selectedSize = size
                            gameStarted = true
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text("${size}x$size")
                    }
                }
            }
        } else {
            GameBoard(
                dim = selectedSize,
                gameScore = gameScore,
                onScoreUpdate = { newScore -> gameScore = newScore },
                onShowScore = { showScoreDialog = true },
                onNewGame = { 
                    gameStarted = false
                    showScoreDialog = false  // Закриваємо діалог при поверненні до вибору поля
                }
            )

            // Score Dialog - перенесено всередину блоку else
            if (showScoreDialog) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .border(2.dp, MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Game Statistics",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "X Wins: ${gameScore.xWins}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "O Wins: ${gameScore.oWins}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = "Draws: ${gameScore.draws}",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Button(
                            onClick = { showScoreDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp)
                        ) {
                            Text("Close")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameBoard(
    dim: Int,
    gameScore: GameScore,
    onScoreUpdate: (GameScore) -> Unit,
    onShowScore: () -> Unit,
    onNewGame: () -> Unit
) {
    val field = remember { mutableStateListOf(*Array(dim * dim) { "_" }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameState by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(10) }
    var isTimerActive by remember { mutableStateOf(true) }
    
    // Timer effect
    LaunchedEffect(currentPlayer, gameState) {
        if (gameState.isEmpty()) {
            isTimerActive = true
            timeLeft = 10
            while (timeLeft > 0 && isTimerActive) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0 && isTimerActive) {
                // Time's up, switch player
                currentPlayer = if (currentPlayer == "X") "0" else "X"
            }
        }
    }
    
    Column {
        // Display scores
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "X: ${gameScore.xWins}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Draws: ${gameScore.draws}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Text(
                text = "O: ${gameScore.oWins}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        // Game controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Button(
                onClick = {
                    field.clear()
                    field.addAll(Array(dim * dim) { "_" })
                    currentPlayer = "X"
                    gameState = ""
                    isTimerActive = true
                    timeLeft = 10
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Text("Reset Round")
            }
            Button(
                onClick = onShowScore,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Text("Show Score")
            }
            Button(
                onClick = onNewGame,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.dp)
            ) {
                Text("New Game")
            }
        }

        // Display current player and timer
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = "Current Player: $currentPlayer",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            if (gameState.isEmpty()) {
                Text(
                    text = "Time left: $timeLeft seconds",
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                LinearProgressIndicator(
                    progress = timeLeft / 10f,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
            }
        }

        // Display game state if game is over
        if (gameState.isNotEmpty()) {
            Text(
                text = gameState,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center
            )
        }

        // Game board
        for (row in 0 until dim) {
            Row {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    Box(
                        modifier = Modifier.size(80.dp)
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                            .clickable {
                                if (field[index] == "_" && gameState.isEmpty()) {
                                    isTimerActive = false // Stop current timer
                                    field[index] = currentPlayer
                                    
                                    // Check for win
                                    if (checkWin(field, dim, currentPlayer)) {
                                        gameState = "$currentPlayer wins!"
                                        val newScore = gameScore.copy()
                                        when (currentPlayer) {
                                            "X" -> newScore.xWins++
                                            "0" -> newScore.oWins++
                                        }
                                        onScoreUpdate(newScore)
                                    } else if (!field.contains("_")) {
                                        gameState = "It's a draw!"
                                        val newScore = gameScore.copy(draws = gameScore.draws + 1)
                                        onScoreUpdate(newScore)
                                    } else {
                                        currentPlayer = if (currentPlayer == "X") "0" else "X"
                                        isTimerActive = true // Start new timer
                                    }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = field[index],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

private fun checkWin(field: List<String>, dim: Int, player: String): Boolean {
    // Check rows
    for (row in 0 until dim) {
        var win = true
        for (col in 0 until dim) {
            if (field[row * dim + col] != player) {
                win = false
                break
            }
        }
        if (win) return true
    }

    // Check columns
    for (col in 0 until dim) {
        var win = true
        for (row in 0 until dim) {
            if (field[row * dim + col] != player) {
                win = false
                break
            }
        }
        if (win) return true
    }

    // Check main diagonal
    var win = true
    for (i in 0 until dim) {
        if (field[i * dim + i] != player) {
            win = false
            break
        }
    }
    if (win) return true

    // Check anti-diagonal
    win = true
    for (i in 0 until dim) {
        if (field[i * dim + (dim - 1 - i)] != player) {
            win = false
            break
        }
    }
    return win
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    LessonTicTacToeTheme {
        MainScreen()
    }
}