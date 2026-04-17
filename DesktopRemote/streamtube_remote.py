import customtkinter as ctk
import requests
import threading
import time
import socket

# Configuración Visual
ctk.set_appearance_mode("dark")
ctk.set_default_color_theme("blue")

class StreamTubeRemote(ctk.CTk):
    def __init__(self):
        super().__init__()

        self.title("StreamTube 24/7 - Remote Control")
        self.geometry("500x600")

        # Variables de estado
        self.phone_ip = "192.168.1.15"  # Puedes cambiarlo manualmente aquí o el script intentará detectarlo
        self.is_connected = False

        self.setup_ui()
        
        # Hilo de monitoreo
        self.monitor_thread = threading.Thread(target=self.update_loop, daemon=True)
        self.monitor_thread.start()

    def setup_ui(self):
        # Header
        self.header = ctk.CTkLabel(self, text="📺 StreamTube 24/7", font=("Outfit", 26, "bold"), text_color="#FF5252")
        self.header.pack(pady=30)

        # IP Config
        self.ip_frame = ctk.CTkFrame(self)
        self.ip_frame.pack(pady=10, padx=20, fill="x")
        self.ip_entry = ctk.CTkEntry(self.ip_frame, placeholder_text="IP del Celular (ej: 192.168.1.15)")
        self.ip_entry.insert(0, self.phone_ip)
        self.ip_entry.pack(side="left", padx=10, pady=10, expand=True, fill="x")
        self.btn_connect = ctk.CTkButton(self.ip_frame, text="Set IP", width=60, command=self.set_ip)
        self.btn_connect.pack(side="right", padx=10)

        # Status Indicators
        self.status_card = ctk.CTkFrame(self, corner_radius=15, fg_color="#1E1E1E")
        self.status_card.pack(pady=20, padx=40, fill="x")

        self.lbl_status = ctk.CTkLabel(self.status_card, text="🔌 ESTADO: DESCONECTADO", font=("Outfit", 14, "bold"), text_color="grey")
        self.lbl_status.pack(pady=(15, 5))

        self.lbl_uptime = ctk.CTkLabel(self.status_card, text="Uptime: 0h 0m", font=("Outfit", 12))
        self.lbl_uptime.pack(pady=5)

        self.lbl_temp = ctk.CTkLabel(self.status_card, text="Temperatura: -- °C", font=("Outfit", 12))
        self.lbl_temp.pack(pady=5)

        self.lbl_file = ctk.CTkLabel(self.status_card, text="Archivo Actual: Ninguno", font=("Outfit", 11), text_color="#AAAAAA")
        self.lbl_file.pack(pady=(5, 15))

        # Control Buttons
        self.btn_start = ctk.CTkButton(self, text="▶ INICIAR STREAMING", font=("Outfit", 16, "bold"), 
                                     fg_color="#2EB82E", hover_color="#248F24", height=50, command=lambda: self.send_act("start"))
        self.btn_start.pack(pady=10, padx=60, fill="x")

        self.btn_stop = ctk.CTkButton(self, text="⏹ DETENER STREAMING", font=("Outfit", 16, "bold"), 
                                    fg_color="#E63946", hover_color="#B32D38", height=50, command=lambda: self.send_act("stop"))
        self.btn_stop.pack(pady=10, padx=60, fill="x")

        self.lbl_footer = ctk.CTkLabel(self, text="Powered by Reggaetronix Engine", font=("Outfit", 10), text_color="#444444")
        self.lbl_footer.pack(side="bottom", pady=20)

    def set_ip(self):
        self.phone_ip = self.ip_entry.get()
        print(f"IP actualizada a: {self.phone_ip}")

    def send_act(self, action):
        try:
            requests.post(f"http://{self.phone_ip}:8080/control/{action}", timeout=3)
        except Exception as e:
            print(f"Error de conexión: {e}")

    def update_loop(self):
        while True:
            try:
                r = requests.get(f"http://{self.phone_ip}:8080/status", timeout=2)
                if r.status_code == 200:
                    data = r.json()
                    self.is_connected = True
                    # Actualizar UI
                    status_text = "🔴 EN VIVO" if data['isStreaming'] else "🟢 CONECTADO (IDLE)"
                    status_color = "#FF5252" if data['isStreaming'] else "#2EB82E"
                    
                    self.lbl_status.configure(text=f"🔌 ESTADO: {status_text}", text_color=status_color)
                    self.lbl_uptime.configure(text=f"Uptime: {data['uptime']}")
                    self.lbl_temp.configure(text=f"Temperatura: {data['cpuTemp']} °C")
                    self.lbl_file.configure(text=f"Archivo Actual: {data['currentFile']}")
                else:
                    self.is_connected = False
            except:
                self.is_connected = False
                self.lbl_status.configure(text="🔌 ESTADO: DESCONECTADO", text_color="grey")
            
            time.sleep(3)

if __name__ == "__main__":
    app = StreamTubeRemote()
    app.mainloop()
