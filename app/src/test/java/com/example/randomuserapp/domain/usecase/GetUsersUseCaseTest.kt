package com.example.randomuserapp.domain.usecase

import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.domain.repository.UserRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class GetUsersUseCaseTest{
    private val mockRepository = mock(UserRepository::class.java)
    private val getUsersUseCase = GetUsersUseCase(mockRepository)

    @Test
    fun `invoke returns success`() = runBlocking {
        // Mock repository response
        val mockUsers = listOf(User("1", "John", "Doe", "123 Main St", "thumbnail.jpg"))
        `when`(mockRepository.getUsers(1)).thenReturn(mockUsers)

        // Call the use case
        val result = getUsersUseCase(1)

        // Verify and assert
        verify(mockRepository).getUsers(1)
        assertTrue(result.isSuccess)
        assertEquals(mockUsers, result.getOrNull())
    }

    @Test
    fun `invoke returns failure`() = runBlocking {
        // Mock repository exception
        val exception = Exception("Network error")
        `when`(mockRepository.getUsers(1)).thenThrow(exception)

        // Call the use case
        val result = getUsersUseCase(1)

        // Verify and assert
        verify(mockRepository).getUsers(1)
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}