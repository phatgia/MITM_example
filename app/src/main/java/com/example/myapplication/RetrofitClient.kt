package com.example.myapplication

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // TODO: Thay thế đường dẫn này bằng HTTPS URL của ngrok của bạn (ví dụ: https://abcd-123.ngrok-free.app)
    private const val BASE_URL = "https://225e-113-173-224-26.ngrok-free.app"

    fun getInstance(enablePinning: Boolean): ApiService {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS) // Cho phép chờ 2 phút
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)    // Để bạn có dư dả thời gian
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)   // sửa nội dung trên Burp Suite

        // Giai đoạn 3: Bật Certificate Pinning ở đây
        if (enablePinning) {
            // Lấy hostname từ BASE_URL (Bỏ https://)
            val hostname = BASE_URL.replace("https://", "").replace("/", "")
            
            val certificatePinner = CertificatePinner.Builder()
                // Mã băm (Hash) thật của server ngrok hiện tại
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
