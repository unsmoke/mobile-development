package com.cpp.unsmoke.ui.main

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.cpp.unsmoke.R
import com.cpp.unsmoke.databinding.ActivityMainBinding
import com.cpp.unsmoke.ui.ismoke.IsmokeActivity
import com.cpp.unsmoke.ui.notification.AlarmInfo
import com.cpp.unsmoke.ui.notification.MyDailyReminderReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var reminderReceiver: MyDailyReminderReceiver

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.navView.menu.getItem(2).isEnabled = false

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        binding.ismokeBtn.setOnClickListener {
            val intent = Intent(this, IsmokeActivity::class.java)
            startActivity(intent)
        }


        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        reminderReceiver = MyDailyReminderReceiver()

        // Call the method to fetch notification data and set alarms
        fetchNotificationDataAndSetAlarms()

        AutoStartHelper.getInstance().getAutoStartPermission(this)
    }

    private fun fetchNotificationDataAndSetAlarms() {
        CoroutineScope(Dispatchers.IO).launch {
            // Simulating fetching data from an API
            val alarms = fetchAlarmsFromApi()

            withContext(Dispatchers.Main) {
                reminderReceiver.setRepeatingAlarms(this@MainActivity, alarms)
            }
        }
    }

    private suspend fun fetchAlarmsFromApi(): List<AlarmInfo> {
        // Simulate an API call
        return listOf(
            AlarmInfo("00:40", "Good morning! Time to start your day!"),
            AlarmInfo("09:00", "Don't forget to take a lunch break!"),
            AlarmInfo("10:00", "Time to relax and unwind for the evening.")
        )
    }

    companion object {
        const val MY_PREFERENCES = "MyPrefs"
    }
}