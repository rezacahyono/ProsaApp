package com.rchyn.prosa.ui.fragments.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rchyn.prosa.data.repository.user.UserRepository
import com.rchyn.prosa.utils.MainCoroutineRule
import com.rchyn.prosa.utils.UiText
import com.rchyn.prosa.utils.Utilities.whenever
import com.rchyn.prosa.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {


    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var userRepository: UserRepository
    private lateinit var registerViewModel: RegisterViewModel


    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(userRepository)
    }


    @Test
    fun `When Register Should Not Null, And Return Register Ui State Success`() = runTest {
        val expectedState = RegisterUiState(isSuccess = true)
        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(
            userRepository.register(
                name = "Dimas",
                email = "dimas@gmail.com",
                password = "password"
            )
        ).thenReturn(data)

        var actualState = RegisterUiState()
        registerViewModel.register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        ).observeForever { state ->
            actualState = state
        }

        Mockito.verify(userRepository).register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        )
        Assert.assertTrue(actualState.isSuccess)
        Assert.assertFalse(actualState.isError)
        Assert.assertFalse(actualState.isLoading)
        Assert.assertEquals(expectedState.isSuccess, actualState.isSuccess)
    }

    @Test
    fun `When Register Should Not Null, And Return Register Ui State Loading`() = runTest {
        val expectedState = RegisterUiState(isLoading = true)
        val data = flow {
            emit(mapOf(true to UiText.DynamicString("Success")))
        }

        whenever(
            userRepository.register(
                name = "Dimas",
                email = "dimas@gmail.com",
                password = "password"
            )
        ).thenReturn(data)

        val actualState = registerViewModel.register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        ).getOrAwaitValue()

        Mockito.verify(userRepository).register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        )
        Assert.assertTrue(actualState.isLoading)
        Assert.assertFalse(actualState.isError)
        Assert.assertFalse(actualState.isSuccess)
        Assert.assertEquals(expectedState.isLoading, actualState.isLoading)
    }

    @Test
    fun `When Register Should Not Null, And Return Register Ui State Error`() = runTest {
        val expectedState = RegisterUiState(isError = true, messageError = UiText.DynamicString("Error"))
        val data = flow {
            emit(mapOf(false to UiText.DynamicString("Error")))
        }

        whenever(
            userRepository.register(
                name = "Dimas",
                email = "dimas@gmail.com",
                password = "password"
            )
        ).thenReturn(data)

        var actualState = RegisterUiState()
        registerViewModel.register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        ).observeForever { state ->
            actualState = state
        }

        Mockito.verify(userRepository).register(
            name = "Dimas",
            email = "dimas@gmail.com",
            password = "password"
        )
        Assert.assertTrue(actualState.isError)
        Assert.assertFalse(actualState.isSuccess)
        Assert.assertFalse(actualState.isLoading)
        Assert.assertEquals(
            (expectedState.messageError as UiText.DynamicString).value,
            (actualState.messageError as UiText.DynamicString).value
        )
        Assert.assertEquals(expectedState.isError, actualState.isError)
    }
}