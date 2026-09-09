//This file are the different states of the Login UI
package com.example.project01_group05.ui.login

sealed interface LoginUiState {
    data object Idle : LoginUiState
    data object Loading : LoginUiState
    data class Success(val username: String) : LoginUiState
    data class Error(val message: String) : LoginUiState
}

