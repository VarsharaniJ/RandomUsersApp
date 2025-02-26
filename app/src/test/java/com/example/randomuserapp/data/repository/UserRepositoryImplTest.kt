package com.example.randomuserapp.data.repository

import com.example.randomuserapp.data.api.RandomUserApi
import com.example.randomuserapp.data.model.Coordinates
import com.example.randomuserapp.data.model.Dob
import com.example.randomuserapp.data.model.Id
import com.example.randomuserapp.data.model.Info
import com.example.randomuserapp.data.model.Location
import com.example.randomuserapp.data.model.Login
import com.example.randomuserapp.data.model.Name
import com.example.randomuserapp.data.model.Picture
import com.example.randomuserapp.data.model.RandomUserResponse
import com.example.randomuserapp.data.model.Registered
import com.example.randomuserapp.data.model.Street
import com.example.randomuserapp.data.model.Timezone
import com.example.randomuserapp.data.model.UserResult
import com.example.randomuserapp.domain.model.User
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`


class UserRepositoryImplTest{

    private val mockApi = mock(RandomUserApi::class.java)
    private val userRepository = UserRepositoryImpl(mockApi)


    @Test
    fun `fetch users successfully`() = runBlocking {
        // Mock API response
        val apiResponse = RandomUserResponse(
            results = listOf(
                UserResult(
                    gender = "male",
                    name = Name("Mr", "John", "Doe"),
                    location = Location(
                        street = Street(123, "Main Street"),
                        city = "Cityville",
                        state = "State",
                        country = "Country",
                        postcode = "12345",
                        coordinates = Coordinates("0.0", "0.0"),
                        timezone = Timezone("+0:00", "UTC")
                    ),
                    email = "john.doe@example.com",
                    login = Login("uuid", "username", "password", "salt", "md5", "sha1", "sha256"),
                    dob = Dob("1990-01-01", 33),
                    registered = Registered("2020-01-01", 3),
                    phone = "1234567890",
                    cell = "0987654321",
                    id = Id("ID", "123"),
                    picture = Picture("large.jpg", "medium.jpg", "thumbnail.jpg"),
                    nat = "US"
                )
            ),
            info = Info("seed", 1, 1, "1.0")
        )
        `when`(mockApi.getUsers(1)).thenReturn(apiResponse)

        // Call the repository method
        val result = userRepository.getUsers(1)

        // Verify the result
        val expected = listOf(
            User(
                id = "John-Doe",
                firstName = "John",
                lastName = "Doe",
                address = "123 Main Street, Cityville",
                profilePicture = "thumbnail.jpg"
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `handle empty response`() = runBlocking {
        // Mock API response with no results
        val apiResponse = RandomUserResponse(results = emptyList(), info = Info("seed", 0, 1, "1.0"))
        `when`(mockApi.getUsers(1)).thenReturn(apiResponse)

        // Call the repository method
        val result = userRepository.getUsers(1)

        // Verify the result
        assertEquals(emptyList<User>(), result)
    }

    @Test
    fun `handle API exceptions`() = runBlocking {
        // Mock API exception
        `when`(mockApi.getUsers(1)).thenThrow(RuntimeException("Network error"))

        // Verify the exception is thrown
        val exception = assertThrows(RuntimeException::class.java) {
            runBlocking { userRepository.getUsers(1) }
        }
        assertEquals("Network error", exception.message)
    }

    @Test
    fun `handle malformed data`() = runBlocking {
        // Mock API response with null values
        val apiResponse = RandomUserResponse(
            results = listOf(
                UserResult(
                    gender = "male",
                    name = Name("Mr", "John", ""),
                    location = Location(
                        street = Street(123, ""),
                        city = "Cityville",
                        state = "State",
                        country = "Country",
                        postcode = "12345",
                        coordinates = Coordinates("0.0", "0.0"),
                        timezone = Timezone("+0:00", "UTC")
                    ),
                    email = "john.doe@example.com",
                    login = Login("uuid", "username", "password", "salt", "md5", "sha1", "sha256"),
                    dob = Dob("1990-01-01", 33),
                    registered = Registered("2020-01-01", 3),
                    phone = "1234567890",
                    cell = "0987654321",
                    id = Id("ID", null),
                    picture = Picture("large.jpg", "medium.jpg", ""),
                    nat = "US"
                )
            ),
            info = Info("seed", 1, 1, "1.0")
        )
        `when`(mockApi.getUsers(1)).thenReturn(apiResponse)

        // Call the repository method
        val result = userRepository.getUsers(1)

        // Verify the result
        val expected = listOf(
            User(
                id = "John-",
                firstName = "John",
                lastName = "",
                address = "123 , Cityville",
                profilePicture = ""
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `getUsers returns transformed data`() = runBlocking {
        // Mock API response
        val mockResponse = RandomUserResponse(
            results = listOf(
                UserResult(
                    gender = "male",
                    name = Name("Mr", "John", "Doe"),
                    location = Location(
                        street = Street(123, "Main St"),
                        city = "New York",
                        state = "NY",
                        country = "USA",
                        postcode = "12345",
                        coordinates = Coordinates("40.7128", "-74.0060"),
                        timezone = Timezone("-5:00", "Eastern Time")
                    ),
                    email = "john.doe@example.com",
                    login = Login("uuid1", "johndoe", "pass", "salt", "md5", "sha1", "sha256"),
                    dob = Dob("1990-01-01", 33),
                    registered = Registered("2020-01-01", 3),
                    phone = "123-456-7890",
                    cell = "098-765-4321",
                    id = Id("SSN", "123-45-6789"),
                    picture = Picture("large.jpg", "medium.jpg", "thumbnail.jpg"),
                    nat = "US"
                )
            ),
            info = Info("seed1", 1, 1, "1.0")
        )
        `when`(mockApi.getUsers(1)).thenReturn(mockResponse)

        // Call the repository
        val result = userRepository.getUsers(1)

        // Verify and assert
        verify(mockApi).getUsers(1)
        assertEquals(1, result.size)
        assertEquals(User("John-Doe", "John", "Doe", "123 Main St, New York", "thumbnail.jpg"), result[0])
    }
}