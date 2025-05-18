package com.example.sem2androidproject.domain

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.sem2androidproject.R
import com.example.sem2androidproject.domain.model.NoteModel
import javax.inject.Inject

class ReminderWorker@Inject constructor(private val repository: INoteRepository,context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val noteId = inputData.getLong("NOTE_ID", -1)
        if (noteId == -1L) return Result.failure()

        return try {
            val note = repository.getNoteById(noteId)
            note?.let {
                showNotification(it)
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }

    }
    private fun showNotification(note: NoteModel) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "reminder_channel"


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(note.amount.toString())
            .setContentText(note.noteBody)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(note.id.toInt(), notification)
    }

}
