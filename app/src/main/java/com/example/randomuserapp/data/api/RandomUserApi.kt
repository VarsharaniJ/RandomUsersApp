package com.example.randomuserapp.data.api

import com.example.randomuserapp.data.model.RandomUserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RandomUserApi {
    @GET("api/")
    suspend fun getUsers(@Query("results") count: Int): RandomUserResponse
}

