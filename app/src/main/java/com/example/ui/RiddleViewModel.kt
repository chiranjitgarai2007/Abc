package com.example.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Riddle(
    val levelNumber: Int,
    val question: String,
    val answer: String,
    val hint: String,
    val explanation: String
)

class RiddleViewModel : ViewModel() {
    val riddles = listOf(
        Riddle(
            levelNumber = 1,
            question = "4, 8, 16, ?",
            answer = "32",
            hint = "Pattern: Multiply by 2\n\n4 x 2 = 8\n8 x 2 = 16\n16 x 2 = ?",
            explanation = "The sequence doubles each step.\n4 -> 8 -> 16 -> 32"
        ),
        Riddle(
            levelNumber = 2,
            question = "3, 6, 12, 24, ?",
            answer = "48",
            hint = "Pattern: Each number doubles\n\n3 x 2 = 6\n6 x 2 = 12\n12 x 2 = 24\n24 x 2 = ?",
            explanation = "The sequence doubles each step.\n3 -> 6 -> 12 -> 24 -> 48"
        ),
        Riddle(
            levelNumber = 3,
            question = "2, 5, 10, 17, ?",
            answer = "26",
            hint = "Pattern: Add consecutive odd numbers\n\n2 + 3 = 5\n5 + 5 = 10\n10 + 7 = 17\n17 + 9 = ?",
            explanation = "Add consecutive odd numbers.\n+3, +5, +7, +9\n17 + 9 = 26"
        ),
        Riddle(
            levelNumber = 4,
            question = "1, 4, 9, 16, ?",
            answer = "25",
            hint = "Pattern: Perfect square numbers\n\n1^2 = 1\n2^2 = 4\n3^2 = 9\n4^2 = 16\n5^2 = ?",
            explanation = "The sequence is perfect squares.\n1, 4, 9, 16, 25"
        ),
        Riddle(
            levelNumber = 5,
            question = "5, 10, 20, 40, ?",
            answer = "80",
            hint = "Pattern: Multiply by 2\n\n5 x 2 = 10\n10 x 2 = 20\n20 x 2 = 40\n40 x 2 = ?",
            explanation = "The sequence doubles each step.\n5 -> 10 -> 20 -> 40 -> 80"
        )
    )

    private val _currentLevelIndex = MutableStateFlow(0)
    val currentLevelIndex: StateFlow<Int> = _currentLevelIndex.asStateFlow()

    private val _currentAnswer = MutableStateFlow("")
    val currentAnswer: StateFlow<String> = _currentAnswer.asStateFlow()

    private val _isLevelSuccess = MutableStateFlow(false)
    val isLevelSuccess: StateFlow<Boolean> = _isLevelSuccess.asStateFlow()

    private val _isGameCompleted = MutableStateFlow(false)
    val isGameCompleted: StateFlow<Boolean> = _isGameCompleted.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError.asStateFlow()

    private val _showHint = MutableStateFlow(false)
    val showHint: StateFlow<Boolean> = _showHint.asStateFlow()

    fun inputDigit(digit: String) {
        if (_isLevelSuccess.value || _isGameCompleted.value) return
        _showError.value = false
        if (_currentAnswer.value.length < 5) {
            _currentAnswer.value += digit
        }
    }

    fun deleteDigit() {
        if (_isLevelSuccess.value || _isGameCompleted.value) return
        _showError.value = false
        if (_currentAnswer.value.isNotEmpty()) {
            _currentAnswer.value = _currentAnswer.value.dropLast(1)
        }
    }

    fun submitAnswer() {
        if (_isLevelSuccess.value || _isGameCompleted.value) return
        val currentRiddle = riddles[_currentLevelIndex.value]
        if (_currentAnswer.value == currentRiddle.answer) {
            _isLevelSuccess.value = true
        } else {
            _showError.value = true
        }
    }

    fun nextLevel() {
        if (_currentLevelIndex.value < riddles.size - 1) {
            _currentLevelIndex.value += 1
            _currentAnswer.value = ""
            _isLevelSuccess.value = false
        } else {
            _isLevelSuccess.value = false
            _isGameCompleted.value = true
        }
    }

    fun restartGame() {
        _currentLevelIndex.value = 0
        _currentAnswer.value = ""
        _isLevelSuccess.value = false
        _isGameCompleted.value = false
        _showError.value = false
        _showHint.value = false
    }

    fun toggleHint() {
        if (_isLevelSuccess.value || _isGameCompleted.value) return
        _showHint.value = !_showHint.value
    }

    fun dismissError() {
        _showError.value = false
    }

    fun dismissHint() {
        _showHint.value = false
    }
}
