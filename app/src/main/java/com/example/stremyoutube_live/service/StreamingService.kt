package com.example.stremyoutube_live.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.stremyoutube_live.R
import com.example.stremyoutube_live.network.RemoteServer
import com.example.stremyoutube_live.network.StreamStatus
import com.example.stremyoutube_live.media.FFmpegEncoder
import java.io.File

class StreamingService : Service() {
    private val remoteServer = RemoteServer { handleRemoteCommand(it) }
    private val encoder = FFmpegEncoder()
    private var isStreaming = false
    private var startTime: Long = 0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val streamKey = intent?.getStringExtra("STREAM_KEY") ?: ""
        val playlistPath = intent?.getStringExtra("PLAYLIST_PATH") ?: ""

        startForeground(1, buildNotification("Servidor 24/7 Listo"))

        remoteServer.start {
            StreamStatus(
                isStreaming = isStreaming,
                currentFile = encoder.currentFileName,
                uptime = if (isStreaming) formatUptime() else "0h 0m",
                cpuTemp = 41.2f, // Estimado de temperatura
                bitrate = "2500 kbps"
            )
        }

        if (streamKey.isNotEmpty()) {
            startStreaming(streamKey, playlistPath)
        }

        return START_STICKY
    }

    private fun handleRemoteCommand(command: String) {
        when (command) {
            "start" -> { /* Necesitaría pasar la KEY por API o persistencia */ }
            "stop" -> stopStreaming()
        }
    }

    private fun startStreaming(key: String, path: String) {
        if (!isStreaming) {
            encoder.start(key, path)
            isStreaming = true
            startTime = System.currentTimeMillis()
            updateNotification("En Vivo en YouTube")
        }
    }

    private fun stopStreaming() {
        encoder.stop()
        isStreaming = false
        updateNotification("Transmisión Detenida")
    }

    private fun formatUptime(): String {
        val seconds = (System.currentTimeMillis() - startTime) / 1000
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        return "${hours}h ${minutes}m"
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                "STREAM_CHANNEL", "Transmisión 24/7",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    private fun buildNotification(text: String): Notification {
        return NotificationCompat.Builder(this, "STREAM_CHANNEL")
            .setContentTitle("StreamTube 24/7")
            .setContentText(text)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .build()
    }

    private fun updateNotification(text: String) {
        val manager = getSystemService(NotificationManager::class.java)
        manager?.notify(1, buildNotification(text))
    }

    override fun onDestroy() {
        remoteServer.stop()
        encoder.stop()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
