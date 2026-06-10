package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class Riddle(
    val levelNumber: Int,
    val question: String,
    val answer: String,
    val hint: String,
    val explanation: String
)

class RiddleViewModel(application: Application) : AndroidViewModel(application) {
    val storage = StorageService(application)
    val riddles = listOf(
        Riddle(
            levelNumber = 1,
            question = "If you multiply me by any other number, the result will always remain the same. What number am I?",
            answer = "0",
            hint = "Think about the multiplicative property of zero. Any number times this value is always this value.",
            explanation = "Multiplying any number by 0 always equals 0 (e.g., 5 x 0 = 0)."
        ),
        Riddle(
            levelNumber = 2,
            question = "I am a three-digit number. My hundreds digit is 2. My tens digit is 3 times my hundreds. My ones digit is 4 less than my tens. What number am I?",
            answer = "262",
            hint = "Determine each digit step-by-step:\nHundreds = 2\nTens = 3 * 2\nOnes = Tens - 4\nNow combine them.",
            explanation = "Hundreds digit is 2.\nTens digit is 2 x 3 = 6.\nOnes digit is 6 - 4 = 2.\nCombining them gives 262."
        ),
        Riddle(
            levelNumber = 3,
            question = "A farmer has 17 sheep. All but 9 of them run away. How many sheep does the farmer have left?",
            answer = "9",
            hint = "Read the wording very carefully: 'All but 9' run away.",
            explanation = "The sentence describes 'all but nine' running away, which directly means exactly 9 sheep are left."
        ),
        Riddle(
            levelNumber = 4,
            question = "A grandfather, two fathers, and two sons went to a movie together. They bought exactly 3 tickets. Each had one ticket. How many people went in total?",
            answer = "3",
            hint = "Think of a family tree. How can someone be both a father and a son at the same time?",
            explanation = "The group consists of 3 people from 3 generations: a grandfather, his son (who is a father), and his grandson (who is a son)."
        ),
        Riddle(
            levelNumber = 5,
            question = "A water lily doubles in size every day. If it takes 48 days to fully cover a pond, how many days does it take to cover exactly half of the pond?",
            answer = "47",
            hint = "If the lily doubles every day, what fraction of the pond was covered the day right before it became fully covered?",
            explanation = "Since the lily doubles in size every day, on the 47th day the pond was half covered, doubling to full coverage on the 48th day."
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

    val highestUnlockedLevel = storage.currentLevel
    val soundEnabled = storage.soundEnabled
    val vibrationEnabled = storage.vibrationEnabled

    init {
        viewModelScope.launch {
            storage.currentLevel.collectLatest { level ->
                if (!_isGameCompleted.value && level < riddles.size) {
                    _currentLevelIndex.value = level
                }
            }
        }
        viewModelScope.launch {
            storage.gameCompleted.collectLatest { completed ->
                _isGameCompleted.value = completed
            }
        }
    }

    fun loadSavedGame() {
        // Loads from datastore (already doing via flow, just clear answers)
        _currentAnswer.value = ""
        _isLevelSuccess.value = false
        _showError.value = false
        _showHint.value = false
    }

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
            val nextIdx = _currentLevelIndex.value + 1
            _currentLevelIndex.value = nextIdx
            _currentAnswer.value = ""
            _isLevelSuccess.value = false
            viewModelScope.launch {
                storage.saveCurrentLevel(nextIdx)
            }
        } else {
            _isLevelSuccess.value = false
            _isGameCompleted.value = true
            viewModelScope.launch {
                storage.saveGameCompleted(true)
            }
        }
    }

    fun restartGame() {
        _currentLevelIndex.value = 0
        _currentAnswer.value = ""
        _isLevelSuccess.value = false
        _isGameCompleted.value = false
        _showError.value = false
        _showHint.value = false
        viewModelScope.launch {
            storage.resetProgress()
        }
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
