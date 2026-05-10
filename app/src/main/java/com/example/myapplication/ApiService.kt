package com.example.myapplication

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Models
data class LoginRequest(val username: String, val password: String)
data class LoginResponse(val success: Boolean, val token: String?, val message: String?)

data class MessageRequest(val token: String, val content: String)
data class MessageResponse(val success: Boolean, val message: String?, val receivedData: String?)

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/messages")
    suspend fun sendMessage(@Body request: MessageRequest): Response<MessageResponse>
}
