package com.example.randomuserapp.data.repository

import com.example.randomuserapp.data.api.RandomUserApi
import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val api: RandomUserApi) : UserRepository {
    override suspend fun getUsers(count: Int): List<User> {
        val response = api.getUsers(count)
        return response.results.map { apiUser ->
            User(
                id = "${apiUser.name.first}-${apiUser.name.last}",
                firstName = apiUser.name.first,
                lastName = apiUser.name.last,
                address = "${apiUser.location.street.number} ${apiUser.location.street.name}, ${apiUser.location.city}",
                profilePicture = apiUser.picture.thumbnail
            )
        }
    }
}