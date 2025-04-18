package com.example.lessontictactoe

var DIM = 3

enum class GameState {
    IN_PROGRESS,
    CROSS_WIN,
    NOUGHT_WIN,
    DRAW
}

fun checkGameState(field: List<CellState>): GameState {
    for (row in 0 until DIM) {
        val start = row * DIM
        if (checkLine(field, start, 1)) {
            return when (field[start]) {
                CellState.CROSS -> GameState.CROSS_WIN
                CellState.NOUGHT -> GameState.NOUGHT_WIN
                else -> GameState.IN_PROGRESS
            }
        }
    }

    for (col in 0 until DIM) {
        if (checkLine(field, col, DIM)) {
            return when (field[col]) {
                CellState.CROSS -> GameState.CROSS_WIN
                CellState.NOUGHT -> GameState.NOUGHT_WIN
                else -> GameState.IN_PROGRESS
            }
        }
    }

    if (checkLine(field, 0, DIM + 1)) {
        return when (field[0]) {
            CellState.CROSS -> GameState.CROSS_WIN
            CellState.NOUGHT -> GameState.NOUGHT_WIN
            else -> GameState.IN_PROGRESS
        }
    }

    if (checkLine(field, DIM - 1, DIM - 1)) {
        return when (field[DIM - 1]) {
            CellState.CROSS -> GameState.CROSS_WIN
            CellState.NOUGHT -> GameState.NOUGHT_WIN
            else -> GameState.IN_PROGRESS
        }
    }

    return if (field.any { it == CellState.EMPTY }) {
        GameState.IN_PROGRESS
    } else {
        GameState.DRAW
    }
}

private fun checkLine(field: List<CellState>, start: Int, step: Int): Boolean {
    val first = field[start]
    if (first == CellState.EMPTY) return false
    
    var current = start
    for (i in 1 until DIM) {
        current += step
        if (field[current] != first) return false
    }
    return true
}

enum class Player {
    CROSS,
    NOUGHT
}

val Player.mark: CellState
    get() = when(this) {
        Player.CROSS -> CellState.CROSS
        Player.NOUGHT -> CellState.NOUGHT
    }

enum class CellState {
    EMPTY,
    CROSS,
    NOUGHT
}

fun printField(field: List<CellState>) {
    for (row in 0 until DIM) {
        for (col in 0 until DIM) {
            val index = row * DIM + col
            val symbol = when(field[index]) {
                CellState.EMPTY -> "_"
                CellState.CROSS -> "X"
                CellState.NOUGHT -> "0"
            }
            print("$symbol ")
        }
        println()
    }
}

fun main() {
    println("Select board size (3-5):")
    DIM = readln().toInt()
    if (DIM !in 3..5) {
        println("Invalid size. Using default 3x3.")
        DIM = 3
    }

    val field = MutableList(DIM * DIM) { CellState.EMPTY }
    var currentPlayer = Player.CROSS

    while (true) {
        printField(field)
        println("Player's move ${if(currentPlayer == Player.CROSS) "X" else "0"}")
        println("Enter the move (row and col, separated by space)")
        
        val input = readln()
        val (row, col) = input.split(" ").map { it.toInt() }
        
        if (row !in 0 until DIM || col !in 0 until DIM) {
            println("Invalid move. Try again.")
            continue
        }

        val index = row * DIM + col
        if (field[index] != CellState.EMPTY) {
            println("Cell already taken. Try again.")
            continue
        }

        field[index] = currentPlayer.mark

        val state = checkGameState(field)
        when(state) {
            GameState.CROSS_WIN -> {
                printField(field)
                println("X wins!")
                break
            }
            GameState.NOUGHT_WIN -> {
                printField(field)
                println("0 wins!")
                break
            }
            GameState.DRAW -> {
                printField(field)
                println("It's a draw!")
                break
            }
            GameState.IN_PROGRESS -> {
                currentPlayer = if(currentPlayer == Player.CROSS) Player.NOUGHT else Player.CROSS
            }
        }
    }
}