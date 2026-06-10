package com.example.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RiddleViewModel : ViewModel() {
    private val _currentAnswer = MutableStateFlow("")
    val currentAnswer: StateFlow<String> = _currentAnswer.asStateFlow()

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess.asStateFlow()

    private val _showError = MutableStateFlow(false)
    val showError: StateFlow<Boolean> = _showError.asStateFlow()

    private val _showHint = MutableStateFlow(false)
    val showHint: StateFlow<Boolean> = _showHint.asStateFlow()

    fun inputDigit(digit: String) {
        if (_isSuccess.value) return
        _showError.value = false
        if (_currentAnswer.value.length < 5) {
            _currentAnswer.value += digit
        }
    }

    fun deleteDigit() {
        if (_isSuccess.value) return
        _showError.value = false
        if (_currentAnswer.value.isNotEmpty()) {
            _currentAnswer.value = _currentAnswer.value.dropLast(1)
        }
    }

    fun submitAnswer() {
        if (_isSuccess.value) return
        if (_currentAnswer.value == "32") {
            _isSuccess.value = true
        } else {
            _showError.value = true
        }
    }

    fun toggleHint() {
        if (_isSuccess.value) return
        _showHint.value = !_showHint.value
    }

    fun dismissError() {
        _showError.value = false
    }

    fun dismissHint() {
        _showHint.value = false
    }
}
