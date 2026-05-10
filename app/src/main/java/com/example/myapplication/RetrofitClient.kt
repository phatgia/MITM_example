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

        // Giai đoạn 3: Bật Certificate Pinning ở đây
        if (enablePinning) {
            // Lấy hostname từ BASE_URL (Bỏ https://)
            val hostname = BASE_URL.replace("https://", "").replace("/", "")
            
            val certificatePinner = CertificatePinner.Builder()
                // TODO: Thay thế mã băm này bằng mã thực tế lấy từ OpenSSL
                .add(hostname, "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=") 
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
