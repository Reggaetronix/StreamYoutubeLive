package com.example.stremyoutube_live.media

import com.arthenica.mobileffmpeg.FFmpeg
import android.util.Log

class FFmpegEncoder {
    private var currentExecutionId: Long = 0
    var currentFileName: String = "Esperando..."
    
    fun start(streamKey: String, playlistPath: String) {
        val streamUrl = "rtmp://a.rtmp.youtube.com/live2/"
        val fullUrl = "$streamUrl$streamKey"
        
        // Comando profesional para loop 24/7 de 720p 30fps
        // -re: Tiempo real
        // -f concat: Unir archivos sin interrupción
        // -safe 0: Permitir rutas absolutas
        // -g 60: Intervalo de keyframes de 2 segundos (YouTube lo exige)
        val command = """
            -re -f concat -safe 0 -i $playlistPath 
            -vcodec libx264 -preset veryfast -maxrate 2500k -bufsize 5000k 
            -pix_fmt yuv420p -g 60 -acodec aac -ar 44100 -b:a 128k 
            -f flv $fullUrl
        """.trimIndent().replace("\n", " ")

        Log.d("StreamTube", "Ejecutando comando FFmpeg...")
        currentExecutionId = FFmpeg.executeAsync(command) { executionId, returnCode ->
            Log.d("StreamTube", "Sesión terminada. Status: ${returnCode}")
        }
    }

    fun stop() {
        FFmpeg.cancel()
    }
}
