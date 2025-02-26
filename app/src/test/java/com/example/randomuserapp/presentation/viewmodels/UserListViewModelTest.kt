package com.example.randomuserapp.presentation.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.domain.usecase.GetUsersUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class UserListViewModelTest{
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mockUseCase = mock(GetUsersUseCase::class.java)
    private val viewModel = UserListViewModel(mockUseCase)

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchUsers updates users LiveData on success`() = runBlocking {
        // Mock use case response
        val mockUsers = listOf(User("1", "John", "Doe", "123 Main St", "thumbnail.jpg"))
        `when`(mockUseCase(1)).thenReturn(Result.success(mockUsers))

        // Observe LiveData
        val observer = mock(Observer::class.java) as Observer<List<User>>
        viewModel.users.observeForever(observer)

        // Call the method
        viewModel.fetchUsers(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify and assert
        verify(observer).onChanged(mockUsers)
    }

    @Test
    fun `fetchUsers updates error LiveData on failure`() = runBlocking {
        // Mock use case exception
        val exception = Exception("Network error")
        `when`(mockUseCase(1)).thenReturn(Result.failure(exception))

        // Observe LiveData
        val errorObserver = mock(Observer::class.java) as Observer<String>
        viewModel.error.observeForever(errorObserver)

        // Call the method
        viewModel.fetchUsers(1)
        testDispatcher.scheduler.advanceUntilIdle()

        // Verify and assert
        verify(errorObserver).onChanged("Failed to fetch users: Network error")
    }

}