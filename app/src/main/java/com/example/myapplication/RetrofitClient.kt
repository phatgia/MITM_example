package com.example.myapplication

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://9805-113-173-224-58.ngrok-free.app"

    fun getInstance(enablePinning: Boolean): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)

        if (enablePinning) {
            val hostname = BASE_URL.replace("https://", "").replace("/", "")
            
            val certificatePinner = CertificatePinner.Builder()
                .add(hostname, "sha256/9XXndalRJPNi9GGGQIraZDbVmNA1nTdRBGX8OmB/9Ek=") 
                .build()

            clientBuilder.certificatePinner(certificatePinner)
        }

        val client = clientBuilder.build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}
