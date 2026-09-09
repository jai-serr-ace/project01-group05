package com.example.project01_group05

import com.example.project01_group05.ui.login.LoginUiState
import com.example.project01_group05.ui.login.LoginViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LoginViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LoginViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialState_isIdle() {
        assertEquals(LoginUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun login_withValidCredentials_updatesStateToSuccess() = runTest {
        viewModel.login("admin", "admin")
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Success)
        assertEquals("admin", (currentState as LoginUiState.Success).username)
    }

    @Test
    fun login_withInvalidCredentials_updatesStateToError() = runTest {
        viewModel.login("wrongUser", "wrongPass")
        testDispatcher.scheduler.advanceUntilIdle()

        val currentState = viewModel.uiState.value
        assertTrue(currentState is LoginUiState.Error)
        assertEquals("Invalid username or password", (currentState as LoginUiState.Error).message)
    }
}
