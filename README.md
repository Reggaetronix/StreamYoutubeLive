# 📺 StreamTube 24/7 - Manual de Uso

Esta solución se compone de dos partes: una **App de Android** que actúa como servidor y transmisor, y una **App de PC (Python)** para el control remoto.

## 🚀 Requisitos Previos

### 📱 En el Celular Android:
1.  **Directorio de Trabajo:** Crea una carpeta en la memoria interna llamada `StreamTube`.
2.  **Playlist:** Dentro de esa carpeta, crea un archivo llamado `playlist.txt`.
    -   El formato debe ser: `file '/ruta/al/video1.mp4'` (uno por línea).
    -   Ejemplo: `file '/storage/emulated/0/Movies/mi_video.mp4'`
3.  **Habilitar Depuración USB:** (Opcional) para instalar la app desde Android Studio.

### 💻 En la PC (Remoto):
1.  **Python:** Ten instalado Python 3.
2.  **Librerías:** Ejecuta en tu terminal:
    ```bash
    pip install customtkinter requests
    ```

---

## 🛠️ Cómo Compilar e Instalar (Android)

1.  Abre la carpeta del proyecto en **Android Studio**.
2.  Presiona **Sync Project with Gradle Files** (el elefante arriba a la derecha).
3.  Conecta tu celular por USB.
4.  Presiona **Run** (El triángulo verde).

**Nota Importante:** Al ser una app de streaming 24/7, Android pedirá permiso para "Mostrar sobre otras apps" y "Optimización de Batería". Por favor, **desactiva la optimización de batería** para que el sistema no cierre la app.

---

## 💻 Uso del Control Remoto (PC)

1.  Abre la app en el celular e ingresa tu **Stream Key** de YouTube.
2.  Dale a "INICIAR VIVO" en el celular (o déjalo listo para recibir órdenes).
3.  En la PC, ejecuta el script de control remoto:
    ```bash
    python DesktopRemote/streamtube_remote.py
    ```
4.  Ingresa la IP que muestra el celular en la pantalla principal (ej: `192.168.1.15`).
5.  ¡Listo! Ya puedes controlar todo desde tu escritorio.

---

## 🧠 Características "PRO" Implementadas

-   **Modo Eco:** Pantalla negra total para ahorrar batería y evitar quemados de pantalla.
-   **Transcodificación 720p:** Todo lo que cargues se convertirá automáticamente a 720p 30fps para que sea fluido.
-   **Bucle Infinito:** El motor FFmpeg leerá el `playlist.txt` en bucle eterno.
-   **Monitoreo Térmico:** El servidor remoto reporta la temperatura del celular para evitar sobrecalentamiento.

---

**Desarrollado por Antigravity para Reggaetronix.**
