package com.yuanchik.sentradefend.presentation.ui


import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.ActivityMainBinding
import com.yuanchik.sentradefend.presentation.ui.history.HistoryFragment
import com.yuanchik.sentradefend.presentation.view.fragments.ScanFragment
import com.yuanchik.sentradefend.presentation.ui.settings.SettingsFragment

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


        initNavigation()
    }

    private fun initNavigation() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ScanFragment())
            .commit()
        binding.bottomNavigation.setOnNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.scan -> {
                    val tag = "scan"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: ScanFragment(), tag)
                    true
                }

                R.id.history -> {
                    val tag = "history"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: HistoryFragment(), tag)
                    true
                }

                R.id.settings -> {
                    val tag = "settings"
                    val fragment = checkFragmentExistence(tag)
                    changeFragment(fragment ?: SettingsFragment(), tag)
                    true
                }

                else -> false
            }
        }
    }


    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }
}

/// Тестирование Api ссылок.
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