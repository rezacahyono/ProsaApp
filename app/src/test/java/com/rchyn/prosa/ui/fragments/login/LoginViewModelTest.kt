package com.rchyn.prosa.ui.fragments.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.utils.*
import com.rchyn.prosa.utils.Utilities.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var loginViewModel: LoginViewModel
    private val userDummy = DataDummy.generateUserDummy()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(userRepository)
    }

    @Test
    fun `When Get User Pref Should Not Null And Return True`() = runTest {
        val dataDummy = userDummy.copy(isLogin = true)
        val expectedUser = flow { emit(dataDummy) }

        whenever(userRepository.userPref).thenReturn(expectedUser)

        val actualUser = loginViewModel.userPref.getOrAwaitValue()

        Mockito.verify(userRepository).userPref
        assertTrue(actualUser)
        assertEquals(dataDummy.isLogin, actualUser)
    }

    @Test
    fun `When Get User Pref Should Not Null And Return False`() = runTest {
        val dataDummy = userDummy.copy(isLogin = false)
        val expectedUser = flow { emit(dataDummy) }

        whenever(userRepository.userPref).thenReturn(expectedUser)

        val actualUser = loginViewModel.userPref.getOrAwaitValue()

        Mockito.verify(userRepository).userPref
        assertFalse(actualUser)
        assertEquals(dataDummy.isLogin, actualUser)
    }

    @Test
    fun `When Login Should Not Null, And Return Login Ui State Success`() = runTest {
        val expectedState = LoginUiState(isSuccess = true)
        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(userRepository.login("dimas@gmail.com", "password")).thenReturn(data)

        var actualState = LoginUiState()
        loginViewModel.login("dimas@gmail.com", "password").observeForever { state ->
            actualState = state
        }

        Mockito.verify(userRepository).login("dimas@gmail.com", "password")
        assertTrue(actualState.isSuccess)
        assertFalse(actualState.isError)
        assertFalse(actualState.isLoading)
        assertEquals(expectedState.isSuccess, actualState.isSuccess)
    }

    @Test
    fun `When Login Should Not Null, And Return Login Ui State Loading`() = runTest {
        val expectedState = LoginUiState(
            isLoading = true
        )
        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(userRepository.login("dimas@gmail.com", "password")).thenReturn(data)

        val actualState = loginViewModel.login("dimas@gmail.com", "password").getOrAwaitValue()

        Mockito.verify(userRepository).login("dimas@gmail.com", "password")
        assertTrue(actualState.isLoading)
        assertFalse(actualState.isError)
        assertFalse(actualState.isSuccess)
        assertEquals(expectedState.isLoading, actualState.isLoading)
    }


    @Test
    fun `When Login Should Not Null, And Return Login Ui State Error`() = runTest {
        val expectedState = LoginUiState(
            isSuccess = false,
            isError = true,
            messageError = UiText.DynamicString("error")
        )
        val data = flow {
            emit(mapOf(false to UiText.DynamicString("error")))
        }

        whenever(userRepository.login("dimas@gmail.com", "password")).thenReturn(data)

        var actualState = LoginUiState()
        loginViewModel.login("dimas@gmail.com", "password").observeForever { state ->
            actualState = state
        }

        Mockito.verify(userRepository).login("dimas@gmail.com", "password")
        assertTrue(actualState.isError)
        assertFalse(actualState.isSuccess)
        assertFalse(actualState.isLoading)
        assertEquals(expectedState.isError, actualState.isError)
        assertEquals(
            (expectedState.messageError as UiText.DynamicString).value,
            (actualState.messageError as UiText.DynamicString).value
        )
    }

    @Test
    fun `When SetUserPref Should Call User Repository setUserPref`() = runTest {
        whenever(userRepository.setUserPref(userDummy)).thenReturn(Unit)

        loginViewModel.setUserPref(userDummy)

        Mockito.verify(userRepository).setUserPref(userDummy)
    }
}