//This file manages logic, state updates, async database operations
package com.example.project01_group05.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.database.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val userDao: UserDao): ViewModel() {
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()


    fun login(username : String, password : String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            //This checks login from the userDAO to see if the username and password matches
            val user = userDao.login(username, password)
            if(user != null) {
                //UserEntity gives us the username and isAdmin value
                _uiState.value = LoginUiState.Success(username= user.username, isAdmin = user.isAdmin)

            } else {
                _uiState.value = LoginUiState.Error("Invalid username or password")
            }

//            if(username == "admin" && password == "admin") {
//                Log.d("LoginViewModel", "Valid username and password")
//                _uiState.value = LoginUiState.Success(username)
//            } else {
//
//                Log.d("LoginViewModel", "Invalid username or password")
//                _uiState.value = LoginUiState.Error("Invalid username or password")
//            }
        }
    }
    //allows loginViewModel to use UserDao
    companion object {
        fun loginViewModelFactory(userDao: UserDao): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(userDao) as T
            }
        }
    }
}
