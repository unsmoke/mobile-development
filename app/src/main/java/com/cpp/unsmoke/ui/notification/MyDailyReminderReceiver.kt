package com.cpp.unsmoke.ui.notification

import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.cpp.unsmoke.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MyDailyReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        val title = if (type.equals(TYPE_ONE_TIME, ignoreCase = true)) TYPE_ONE_TIME else TYPE_REPEATING
        val notifId = intent.getIntExtra(EXTRA_NOTIF_ID, 0)
        if (message != null) {
            showAlarmNotification(context, title, message, notifId)
        }
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val channelId = "Channel_1"
        val channelName = "AlarmManager channel"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.green_no_text)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }

    fun setRepeatingAlarms(context: Context, alarms: List<AlarmInfo>) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for ((index, alarm) in alarms.withIndex()) {
            if (isDateInvalid(alarm.time, TIME_FORMAT)) continue

            val intent = Intent(context, MyDailyReminderReceiver::class.java).apply {
                putExtra(EXTRA_MESSAGE, alarm.message)
                putExtra(EXTRA_TYPE, TYPE_REPEATING)
                putExtra(EXTRA_NOTIF_ID, ID_REPEATING + index)
            }

            val timeArray = alarm.time.split(":").map { it.toInt() }

            Log.d("MyDailyReminderReceiver", "setRepeatingAlarms: ${timeArray[0]} ${timeArray[1]}")

            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, timeArray[0])
                set(Calendar.MINUTE, timeArray[1])
                set(Calendar.SECOND, 0)
            }

            val pendingIntent = PendingIntent.getBroadcast(
                context,
                ID_REPEATING + index,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }

        Toast.makeText(context, "Multiple repeating alarms set up", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }

    companion object {
        const val TYPE_ONE_TIME = "OneTimeAlarm"
        const val TYPE_REPEATING = "RepeatingAlarm"
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        const val EXTRA_NOTIF_ID = "notif_id"

        private const val DATE_FORMAT = "yyyy-MM-dd"
        private const val TIME_FORMAT = "HH:mm"

        private const val ID_ONETIME = 100
        private const val ID_REPEATING = 101
    }
}

data class AlarmInfo(val time: String, val message: String)
