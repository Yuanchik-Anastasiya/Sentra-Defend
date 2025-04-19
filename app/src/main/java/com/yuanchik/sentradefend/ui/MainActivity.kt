package com.yuanchik.sentradefend.ui


import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.ActivityMainBinding
import com.yuanchik.sentradefend.debug.NetworkClient
import com.yuanchik.sentradefend.util.API
import kotlinx.coroutines.launch
import java.sql.DriverManager.println

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        /*
                //Проверка Url
                binding.buttonUrl.setOnClickListener {
                    lifecycleScope.launch {
                        try {
                            val response = NetworkClient.virusTotal.scanUrl(API.KEY_VT, "http://example.com")
                            if (response.isSuccessful) {
                                println("✅ URL безопасен: ${response.body()}")
                            } else {
                                println("❌ Ошибка проверки: ${response.errorBody()?.string()}")
                            }
                        } catch (e: Exception) {
                            Log.e("🚨 Исключение: ${e.message}", e.toString())
                        }
                    }
                }

                // Проверка IP
                binding.buttonIP.setOnClickListener {
                    lifecycleScope.launch {
                        try {
                            val response = NetworkClient.fraudLabs.checkIp("8.8.8.8", API.KEY_FLP)

                                if (response.isSuccessful) {
                                    println("✅ FraudLabs OK: ${response.body()}")
                                } else {
                                    println("❌ FraudLabs ошибка: ${response.errorBody()?.string()}")
                                }
                        } catch (e: Exception) {
                            Log.e("🚨 Исключение при запросе: ${e.message}", e.toString())
                        }
                    }
                }
           */
    }
}