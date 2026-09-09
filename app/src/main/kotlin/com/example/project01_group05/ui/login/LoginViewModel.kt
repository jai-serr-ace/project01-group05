//This file manages logic, state updates, async database operations
package com.example.project01_group05.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username : String, password : String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            if(username == "admin" && password == "admin") {
                Log.d("LoginViewModel", "Valid username and password")
                _uiState.value = LoginUiState.Success(username)
            } else {

                Log.d("LoginViewModel", "Invalid username or password")
                _uiState.value = LoginUiState.Error("Invalid username or password")
            }
        }
    }
}
