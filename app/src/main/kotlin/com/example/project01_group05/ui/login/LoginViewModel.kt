//This file manages logic, state updates, async database operations
package com.example.project01_group05.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao? = null) : ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading

            val user: UserEntity? = if (userDao != null) {
                userDao.login(username, password)
            } else {
                // Fallback for previews/mock testing when no UserDao is provided
                if (username == "admin" && password == "admin") {
                    UserEntity(username = "admin", password = "admin")
                } else null
            }

            if (user != null) {
                Log.d("LoginViewModel", "Valid username and password for user: ${user.username}")
                _uiState.value = LoginUiState.Success(username = user.username, isAdmin = user.isAdmin)

            } else {
                Log.d("LoginViewModel", "Invalid username or password")
                _uiState.value = LoginUiState.Error("Invalid username or password")
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    class Factory(private val userDao: UserDao) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
                return LoginViewModel(userDao) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
