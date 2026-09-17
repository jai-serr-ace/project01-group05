package com.example.project01_group05.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.project01_group05.database.UserDao
import com.example.project01_group05.database.entities.UserEntity
import kotlinx.coroutines.launch

@Composable
fun CreateAccountScreen(
    userDao: UserDao? = null,
    onAccountCreated: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Account",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { 
                username = it
                errorMessage = null
            },
            label = { Text("Username") },
            singleLine = true,
            isError = errorMessage?.contains("Username") == true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { 
                password = it
                errorMessage = null
            },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            isError = errorMessage?.contains("Password") == true,
            modifier = Modifier.fillMaxWidth()
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val trimmedUser = username.trim()
                val trimmedPass = password.trim()

                if (trimmedUser.isBlank()) {
                    errorMessage = "Username cannot be empty"
                    return@Button
                }
                if (trimmedPass.isBlank()) {
                    errorMessage = "Password cannot be empty"
                    return@Button
                }

                if (userDao != null) {
                    coroutineScope.launch {
                        val existingUser = userDao.getUserByUsername(trimmedUser)
                        if (existingUser != null) {
                            errorMessage = "Username already exists"
                        } else {
                            userDao.insertUser(UserEntity(username = trimmedUser, password = trimmedPass))
                            Toast.makeText(
                                context,
                                "Account created successfully!",
                                Toast.LENGTH_SHORT
                            ).show()
                            onAccountCreated()
                        }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Account created successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    onAccountCreated()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Create Account")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? Login")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAccountScreenPreview() {
    CreateAccountScreen()
}
