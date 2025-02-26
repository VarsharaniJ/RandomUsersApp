package com.example.randomuserapp.domain.usecase

import com.example.randomuserapp.domain.model.User
import com.example.randomuserapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(private val repository: UserRepository) {
    suspend operator fun invoke(count: Int): Result<List<User>> {
        return try {
            val users = repository.getUsers(count)
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}