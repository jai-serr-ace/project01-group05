package com.example.project01_group05.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminUserViewModel(
    private val userDao: UserDao
) : ViewModel() {

    private val _users = MutableStateFlow<List<UserEntity>>(emptyList())
    val users: StateFlow<List<UserEntity>> = _users.asStateFlow()

    fun loadUsers() {
        viewModelScope.launch {
            _users.value = userDao.getAllUsers()
        }
    }

    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            userDao.deleteUser(user)
            loadUsers()
        }
    }
}