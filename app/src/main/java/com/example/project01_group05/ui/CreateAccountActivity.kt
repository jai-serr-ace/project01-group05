package com.example.project01_group05.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.project01_group05.database.entities.UserEntity
import com.example.project01_group05.databinding.ActivityCreateAccountBinding
import com.example.project01_group05.mangaDB.MangaDB
import kotlinx.coroutines.launch

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDao = MangaDB.getDatabase(applicationContext).userDao()

        binding.btnCreateAccount.setOnClickListener {
            val username = binding.etUsername.text?.toString()?.trim() ?: ""
            val password = binding.etPassword.text?.toString()?.trim() ?: ""

            if (username.isBlank()) {
                binding.tilUsername.error = "Username cannot be empty"
                return@setOnClickListener
            } else {
                binding.tilUsername.error = null
            }

            if (password.isBlank()) {
                binding.tilPassword.error = "Password cannot be empty"
                return@setOnClickListener
            } else {
                binding.tilPassword.error = null
            }

            lifecycleScope.launch {
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser != null) {
                    binding.tilUsername.error = "Username already exists"
                } else {
                    userDao.insertUser(UserEntity(username = username, password = password))
                    Toast.makeText(
                        this@CreateAccountActivity,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }
        }
    }
}
