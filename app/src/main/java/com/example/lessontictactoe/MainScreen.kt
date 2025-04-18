package com.example.lessontictactoe

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lessontictactoe.ui.theme.LessonTicTacToeTheme
import androidx.compose.runtime.*

@Composable
fun MainScreen(modifier: Modifier= Modifier)
{
    Column(modifier=modifier) {
        Text(
            text="Tic Tac Toe",
            style = MaterialTheme.typography.headlineMedium,
            modifier= Modifier
                .fillMaxWidth()
                .padding(16.dp),
            textAlign= TextAlign.Center
        )
    }
    GameBoard()
}

@Composable
fun GameBoard()
{
    val dim=3
    val field= remember { mutableStateListOf(*Array(dim*dim) {"_"}) }
    var currentPlayer by remember { mutableStateOf("X") }
    Column {
        for (row in 0 until dim)
        {
            Row {
                for (col in 0 until dim) {
                    val index = row*dim+col
                    Box(
                        modifier= Modifier.size(80.dp)
                            .padding(4.dp)
                            .border(2.dp,
                                MaterialTheme.colorScheme.primary)
                            .clickable{
                                if(field[index]=="_"){
                                    field[index]=currentPlayer
                                    currentPlayer=if(currentPlayer=="X") "0" else "X"
                                }
                            }
                        ,
                        contentAlignment = Alignment.Center
                    )
                    {
                        Text(
                            text=field[index],
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview()
{
    LessonTicTacToeTheme {
        MainScreen()
    }
}