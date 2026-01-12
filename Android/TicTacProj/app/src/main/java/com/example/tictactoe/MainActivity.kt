package com.example.tictactoe

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

    private enum class Player(val symbol: String) {
        X("❌"),
        O("⭕");

        fun next() = if (this == X) O else X
    }

    private lateinit var cells: List<MaterialButton>
    private lateinit var statusText: TextView
    private lateinit var newGameButton: MaterialButton

    private var currentPlayer = Player.X
    private var gameOver = false

    private val winLines = listOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)

        cells = listOf(
            findViewById(R.id.btn1),
            findViewById(R.id.btn2),
            findViewById(R.id.btn3),
            findViewById(R.id.btn4),
            findViewById(R.id.btn5),
            findViewById(R.id.btn6),
            findViewById(R.id.btn7),
            findViewById(R.id.btn8),
            findViewById(R.id.btn9)
        )

        newGameButton = findViewById(R.id.btnNewGame)
        newGameButton.setOnClickListener { resetGame() }

        cells.forEachIndexed { index, button ->
            button.setOnClickListener { handleMove(index) }
        }

        updateTurnUI()
    }

    private fun handleMove(index: Int) {
        if (gameOver) return

        val cell = cells[index]
        if (!cell.text.isNullOrBlank()) return

        cell.text = currentPlayer.symbol

        when {
            hasWinner(currentPlayer.symbol) -> endGame("Winner: ${currentPlayer.symbol}")
            isDraw() -> endGame("Draw")
            else -> {
                currentPlayer = currentPlayer.next()
                updateTurnUI()
            }
        }
    }

    private fun endGame(message: String) {
        gameOver = true
        statusText.text = message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        setBoardEnabled(false)
    }

    private fun updateTurnUI() {
        statusText.text = "Turn: ${currentPlayer.symbol}"
    }

    private fun hasWinner(symbol: String): Boolean =
        winLines.any { line -> line.all { i -> cells[i].text.toString() == symbol } }

    private fun isDraw(): Boolean =
        cells.all { !it.text.isNullOrBlank() }

    private fun resetGame() {
        cells.forEach { it.text = "" }
        currentPlayer = Player.X
        gameOver = false
        setBoardEnabled(true)
        updateTurnUI()
    }

    private fun setBoardEnabled(enabled: Boolean) {
        cells.forEach { it.isEnabled = enabled }
    }
}
