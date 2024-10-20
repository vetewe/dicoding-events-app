package com.dicoding.dicodingevent.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dicoding.dicodingevent.R
import com.dicoding.dicodingevent.data.retrofit.ApiConfig

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val response = ApiConfig.getApiService().getEvent(active = "-1", limit = "1")
            val event = response.listEvents?.firstOrNull()

            if (event != null) {
                showNotification(event.name ?: "Event", event.beginTime ?: "Time not available")
            } else {
                Log.d("ReminderWorker", "No events found to remind.")
            }

            Result.success()
        } catch (e: Exception) {
            Log.e("ReminderWorker", "Error fetching event data", e)
            Result.failure()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(title: String, content: String) {
        val channelId = "event_reminder_channel"
        val notificationId = 1

        val channel = NotificationChannel(channelId, "Event Reminder", NotificationManager.IMPORTANCE_DEFAULT).apply {
            description = "Channel for event reminders"
        }
        val notificationManager: NotificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.notifications)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(notificationId, builder.build())
        }
    }
}
