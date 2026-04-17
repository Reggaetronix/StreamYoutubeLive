package com.example.stremyoutube_live

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.stremyoutube_live.service.StreamingService

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StreamTubeTheme {
                MainScreen(
                    onStart = { key -> startStream(key) },
                    onStop = { stopStream() }
                )
            }
        }
    }

    private fun startStream(key: String) {
        val intent = Intent(this, StreamingService::class.java).apply {
            putExtra("STREAM_KEY", key)
            putExtra("PLAYLIST_PATH", "/storage/emulated/0/StreamTube/playlist.txt")
        }
        startService(intent)
    }

    private fun stopStream() {
        stopService(Intent(this, StreamingService::class.java))
    }
}

@Composable
fun MainScreen(onStart: (String) -> Unit, onStop: () -> Unit) {
    var streamKey by remember { mutableStateOf("") }
    var isEcoMode by remember { mutableStateOf(false) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = if (isEcoMode) Color.Black else MaterialTheme.colorScheme.background
    ) {
        if (isEcoMode) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("VIVO 24/7 ACTIVO", color = Color.Green, style = MaterialTheme.typography.headlineLarge)
                Text("FPS Estables: 30", color = Color.White)
                Spacer(modifier = Modifier.height(20.dp))
                Button(onClick = { isEcoMode = false }) {
                    Text("SALIR MODO ECO")
                }
            }
        } else {
            Column(modifier = Modifier.padding(20.dp)) {
                Text("StreamTube 24/7", style = MaterialTheme.typography.displaySmall)
                Text("Servidor de Streaming para YouTube", style = MaterialTheme.typography.bodyLarge)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                OutlinedTextField(
                    value = streamKey,
                    onValueChange = { streamKey = it },
                    label = { Text("Stream Key de YouTube") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Configuración de Control Remoto", style = MaterialTheme.typography.titleMedium)
                        Text("PC App: http://TU_IP:8080", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(onClick = { isEcoMode = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)) {
                        Text("Modo Eco")
                    }
                    Button(onClick = { onStart(streamKey) }, colors = ButtonDefaults.buttonColors(containerColor = Color.Red)) {
                        Text("INICIAR VIVO")
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Button(onClick = { onStop() }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)) {
                    Text("DETENER TODO", color = Color.Black)
                }
            }
        }
    }
}

@Composable
fun StreamTubeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFFFF5252),
            background = Color(0xFF121212),
            surface = Color(0xFF1E1E1E)
        ),
        content = content
    )
}