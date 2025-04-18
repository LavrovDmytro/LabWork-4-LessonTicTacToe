package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import kotlinx.coroutines.delay

data class GameScore(
    var xWins: Int = 0,
    var oWins: Int = 0,
    var draws: Int = 0
)

data class PlayerSymbols(
    var player1: String = "X",
    var player2: String = "0"
)

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    var selectedSize by remember { mutableStateOf(3) }
    var gameStarted by remember { mutableStateOf(false) }
    var gameScore by remember { mutableStateOf(GameScore()) }
    var showScoreDialog by remember { mutableStateOf(false) }
    var showSymbolsDialog by remember { mutableStateOf(false) }
    var isDarkTheme by remember { mutableStateOf(false) }
    var playerSymbols by remember { mutableStateOf(PlayerSymbols()) }

    LessonTicTacToeTheme(darkTheme = isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = modifier) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tic Tac Toe",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it }
                    )
                }

                if (!gameStarted) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Select Board Size",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(8.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
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

                        Button(
                            onClick = { showSymbolsDialog = true },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Text("Customize Symbols")
                        }
                    }
                } else {
                    GameBoard(
                        dim = selectedSize,
                        gameScore = gameScore,
                        playerSymbols = playerSymbols,
                        onScoreUpdate = { newScore -> gameScore = newScore },
                        onShowScore = { showScoreDialog = true },
                        onNewGame = { 
                            gameStarted = false
                            showScoreDialog = false
                        }
                    )
                }

                if (showScoreDialog) {
                    Dialog(onDismissRequest = { showScoreDialog = false }) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Text(
                                    text = "Game Statistics",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "${playerSymbols.player1} Wins: ${gameScore.xWins}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                                Text(
                                    text = "${playerSymbols.player2} Wins: ${gameScore.oWins}",
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

                if (showSymbolsDialog) {
                    var tempPlayer1 by remember { mutableStateOf(playerSymbols.player1) }
                    var tempPlayer2 by remember { mutableStateOf(playerSymbols.player2) }

                    Dialog(onDismissRequest = { showSymbolsDialog = false }) {
                        Surface(
                            shape = MaterialTheme.shapes.medium,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
        Text(
                                    text = "Customize Symbols",
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    textAlign = TextAlign.Center
                                )
                                
                                OutlinedTextField(
                                    value = tempPlayer1,
                                    onValueChange = { if (it.length <= 1) tempPlayer1 = it },
                                    label = { Text("Player 1 Symbol") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = tempPlayer2,
                                    onValueChange = { if (it.length <= 1) tempPlayer2 = it },
                                    label = { Text("Player 2 Symbol") },
                                    singleLine = true,
                                    modifier = Modifier
                .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Button(
                                        onClick = { showSymbolsDialog = false }
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = {
                                            if (tempPlayer1.isNotEmpty() && tempPlayer2.isNotEmpty() && tempPlayer1 != tempPlayer2) {
                                                playerSymbols = PlayerSymbols(tempPlayer1, tempPlayer2)
                                                showSymbolsDialog = false
                                            }
                                        }
                                    ) {
                                        Text("Save")
                                    }
                                }
                            }
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
    playerSymbols: PlayerSymbols,
    onScoreUpdate: (GameScore) -> Unit,
    onShowScore: () -> Unit,
    onNewGame: () -> Unit
) {
    val field = remember { mutableStateListOf(*Array(dim * dim) { "_" }) }
    var currentPlayer by remember { mutableStateOf(playerSymbols.player1) }
    var gameState by remember { mutableStateOf("") }
    var timeLeft by remember { mutableStateOf(10) }
    var isTimerActive by remember { mutableStateOf(true) }
    
    LaunchedEffect(currentPlayer, gameState) {
        if (gameState.isEmpty()) {
            isTimerActive = true
            timeLeft = 10
            while (timeLeft > 0 && isTimerActive) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft == 0 && isTimerActive) {
                currentPlayer = if (currentPlayer == playerSymbols.player1) playerSymbols.player2 else playerSymbols.player1
            }
        }
    }
    
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "${playerSymbols.player1}: ${gameScore.xWins}",
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
                text = "${playerSymbols.player2}: ${gameScore.oWins}",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Button(
                onClick = {
                    field.clear()
                    field.addAll(Array(dim * dim) { "_" })
                    currentPlayer = playerSymbols.player1
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

        if (gameState.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = gameState,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = {
                        field.clear()
                        field.addAll(Array(dim * dim) { "_" })
                        currentPlayer = playerSymbols.player1
                        gameState = ""
                        isTimerActive = true
                        timeLeft = 10
                    }
                ) {
                    Text("Next Round")
                }
            }
        }

        for (row in 0 until dim) {
            Row {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .padding(4.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary)
                            .clickable {
                                if (field[index] == "_" && gameState.isEmpty()) {
                                    isTimerActive = false
                                    field[index] = currentPlayer
                                    
                                    if (checkWin(field, dim, currentPlayer)) {
                                        gameState = "$currentPlayer wins!"
                                        val newScore = gameScore.copy()
                                        when (currentPlayer) {
                                            playerSymbols.player1 -> newScore.xWins++
                                            playerSymbols.player2 -> newScore.oWins++
                                        }
                                        onScoreUpdate(newScore)
                                    } else if (!field.contains("_")) {
                                        gameState = "It's a draw!"
                                        val newScore = gameScore.copy(draws = gameScore.draws + 1)
                                        onScoreUpdate(newScore)
                                    } else {
                                        currentPlayer = if (currentPlayer == playerSymbols.player1) 
                                            playerSymbols.player2 else playerSymbols.player1
                                        isTimerActive = true
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

    var win = true
    for (i in 0 until dim) {
        if (field[i * dim + i] != player) {
            win = false
            break
        }
    }
    if (win) return true

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