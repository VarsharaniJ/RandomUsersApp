package com.example.randomuserapp.domain.repository

import com.example.randomuserapp.domain.model.User

interface UserRepository {
    suspend fun getUsers(count: Int): List<User>
}