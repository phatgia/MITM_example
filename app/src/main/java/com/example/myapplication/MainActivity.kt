package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private var authToken: String? = null
    private val TAG = "SecureChat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val etMessage = findViewById<EditText>(R.id.etMessage)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val switchPinning = findViewById<Switch>(R.id.switchPinning)

        btnLogin.setOnClickListener {
            val isPinningEnabled = switchPinning.isChecked
            val api = RetrofitClient.getInstance(isPinningEnabled)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.login(LoginRequest("admin", "123456"))
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            authToken = response.body()?.token
                            tvStatus.text = "Đăng nhập thành công!"
                            Toast.makeText(this@MainActivity, "Đăng nhập OK", Toast.LENGTH_SHORT).show()
                        } else {
                            tvStatus.text = "Đăng nhập thất bại."
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvStatus.text = "Lỗi kết nối: ${e.message}"
                        Log.e(TAG, "Login Error", e)
                    }
                }
            }
        }

        btnSend.setOnClickListener {
            if (authToken == null) {
                Toast.makeText(this, "Vui lòng đăng nhập trước!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val messageContent = etMessage.text.toString()
            val isPinningEnabled = switchPinning.isChecked
            val api = RetrofitClient.getInstance(isPinningEnabled)

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = api.sendMessage(MessageRequest(authToken!!, messageContent))
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful && response.body()?.success == true) {
                            tvStatus.text = "Gửi thành công: Server đã nhận"
                            Toast.makeText(this@MainActivity, "Gửi OK", Toast.LENGTH_SHORT).show()
                        } else {
                            tvStatus.text = "Gửi thất bại."
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        tvStatus.text = "Lỗi kết nối (Bị bắt gói tin?): ${e.message}"
                        Log.e(TAG, "Send Error", e)
                    }
                }
            }
        }
    }
}
