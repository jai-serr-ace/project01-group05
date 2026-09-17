package com.example.project01_group05.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.project01_group05.database.UserDao

class AdminUserViewModelFactory(
    private val userDao: UserDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AdminUserViewModel::class.java)) {
            return AdminUserViewModel(userDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}