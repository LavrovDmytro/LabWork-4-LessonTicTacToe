package com.example.lessontictactoe

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
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
    var selectedSize by remember { mutableIntStateOf(3) }
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
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tic Tac Toe",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { isDarkTheme = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.primary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer
                        )
                    )
                }

                AnimatedVisibility(
                    visible = !gameStarted,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Select Board Size",
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            for (size in 3..5) {
                                ElevatedButton(
                                    onClick = {
                                        selectedSize = size
                                        gameStarted = true
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp),
                                    colors = ButtonDefaults.elevatedButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                ) {
                                    Text(
                                        text = "${size}x$size",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                            }
                        }

                        FilledTonalButton(
                            onClick = { showSymbolsDialog = true },
                            modifier = Modifier.padding(bottom = 16.dp)
                        ) {
                            Text("Customize Symbols")
                        }
                    }
                }

                AnimatedVisibility(
                    visible = gameStarted,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
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
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "Game Statistics",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                StatisticRow(
                                    label = "${playerSymbols.player1} Wins:",
                                    value = gameScore.xWins.toString()
                                )
                                StatisticRow(
                                    label = "${playerSymbols.player2} Wins:",
                                    value = gameScore.oWins.toString()
                                )
                                StatisticRow(
                                    label = "Draws:",
                                    value = gameScore.draws.toString()
                                )
                                FilledTonalButton(
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
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 8.dp
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "Customize Symbols",
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold
                                )
                                
                                OutlinedTextField(
                                    value = tempPlayer1,
                                    onValueChange = { newValue ->
                                        if (newValue.codePointCount(0, newValue.length) <= 1) {
                                            tempPlayer1 = newValue
                                        }
                                    },
                                    label = { Text("Player 1 Symbol") },
                                    singleLine = true,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                                
                                OutlinedTextField(
                                    value = tempPlayer2,
                                    onValueChange = { newValue ->
                                        if (newValue.codePointCount(0, newValue.length) <= 1) {
                                            tempPlayer2 = newValue
                                        }
                                    },
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
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = { showSymbolsDialog = false },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text("Cancel")
                                    }
                                    Button(
                                        onClick = {
                                            if (tempPlayer1.isNotEmpty() && tempPlayer2.isNotEmpty() && tempPlayer1 != tempPlayer2) {
                                                playerSymbols = PlayerSymbols(tempPlayer1, tempPlayer2)
                                                showSymbolsDialog = false
                                            }
                                        },
                                        modifier = Modifier.weight(1f)
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
fun StatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
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
    var timeLeft by remember { mutableIntStateOf(10) }
    var isTimerActive by remember { mutableStateOf(true) }
    val haptic = LocalHapticFeedback.current
    
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
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            }
        }
    }
    
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = playerSymbols.player1,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = gameScore.xWins.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Draws",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = gameScore.draws.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = playerSymbols.player2,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = gameScore.oWins.toString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    field.clear()
                    field.addAll(Array(dim * dim) { "_" })
                    currentPlayer = playerSymbols.player1
                    gameState = ""
                    isTimerActive = true
                    timeLeft = 10
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset")
            }
            FilledTonalButton(
                onClick = {
                    onShowScore()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Score")
            }
            FilledTonalButton(
                onClick = {
                    onNewGame()
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("New")
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Current Player: $currentPlayer",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                if (gameState.isEmpty()) {
                    Text(
                        text = "Time left: $timeLeft seconds",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        progress = { timeLeft / 10f },
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                }
            }
        }

        AnimatedVisibility(
            visible = gameState.isNotEmpty(),
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = gameState,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Button(
                        onClick = {
                            field.clear()
                            field.addAll(Array(dim * dim) { "_" })
                            currentPlayer = playerSymbols.player1
                            gameState = ""
                            isTimerActive = true
                            timeLeft = 10
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    ) {
                        Text("Next Round")
                    }
                }
            }
        }

        for (row in 0 until dim) {
            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                for (col in 0 until dim) {
                    val index = row * dim + col
                    var scale by remember { mutableFloatStateOf(1f) }
                    
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .padding(4.dp)
                            .scale(scale)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                when {
                                    field[index] == "_" -> MaterialTheme.colorScheme.surfaceVariant
                                    field[index] == playerSymbols.player1 -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.secondaryContainer
                                }
                            )
                            .clickable {
                                if (field[index] == "_" && gameState.isEmpty()) {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    isTimerActive = false
                                    field[index] = currentPlayer
                                    
                                    scale = 1.2f
                                    
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
                            style = MaterialTheme.typography.headlineMedium,
                            color = when {
                                field[index] == "_" -> MaterialTheme.colorScheme.onSurfaceVariant
                                field[index] == playerSymbols.player1 -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSecondaryContainer
                            },
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LaunchedEffect(scale) {
                        if (scale > 1f) {
                            delay(100)
                            scale = 1f
                        }
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