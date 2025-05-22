package com.yuanchik.sentradefend.presentation.ui


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.ActivityMainBinding
import com.yuanchik.sentradefend.presentation.ui.history.HistoryFragment
import com.yuanchik.sentradefend.presentation.view.fragments.ScanFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var isNavVisible = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val openHistory = intent.getBooleanExtra("open_history", false)

        createNotificationChannel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.toggleNavButton.setOnClickListener {
            toggleNav()
        }

        initNavigation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNavigation() {
        val openHistory = intent.getBooleanExtra("open_history", false)

        val initialFragment = if (openHistory) HistoryFragment() else ScanFragment()
        val initialTag = if (openHistory) "history" else "scan"

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, initialFragment, initialTag)
            .commit()

        // Устанавливаем активную иконку в BottomNavigation
        binding.bottomNavigation.selectedItemId = if (openHistory) R.id.history else R.id.scan

        binding.bottomNavigation.setOnItemSelectedListener {
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

                else -> false
            }
        }
    }

    private fun checkFragmentExistence(tag: String): Fragment? =
        supportFragmentManager.findFragmentByTag(tag)

    private fun changeFragment(fragment: Fragment, tag: String) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment?.tag == tag) return

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment, tag)
            .addToBackStack(null)
            .commit()
    }

    private fun toggleNav() {
        val nav = binding.bottomNavigation
        val fab = binding.toggleNavButton

        if (isNavVisible) {
            nav.animate()
                .translationY(nav.height.toFloat())
                .setDuration(300)
                .withEndAction {
                    nav.visibility = View.GONE
                    fab.setImageResource(R.drawable.ic_eye_closed) //  Панель скрыта
                }
                .start()
        } else {
            nav.visibility = View.VISIBLE
            nav.animate()
                .translationY(0f)
                .setDuration(300)
                .withEndAction {
                    fab.setImageResource(R.drawable.ic_eye_open) //  Панель видна
                }
                .start()
        }

        isNavVisible = !isNavVisible
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "scan_result_channel",
                "Scan Results",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Уведомления о результатах сканирования"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}