package com.example.extrusor_interfaz_grafica.logica
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread

class BluetoothConnectionService(
    private val device: BluetoothDevice,
    private val context: Context
) {

    private var notice: Notifier = Notifier()
    @Volatile
    private var isListening = false
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    var onConnected: (() -> Unit)? = null
    var onDisconnected: (() -> Unit)? = null
    var onConnectionFailed: ((Exception) -> Unit)? = null
    var onMessageReceived: ((String) -> Unit)? = null

    private var discoveryCancelReceiver: BroadcastReceiver? = null

    @SuppressLint("MissingPermission")
    fun connect() {
        thread {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter?.isDiscovering == true) {
                Log.d("BluetoothConn", "Descubrimiento activo, esperando cancelación...")
                val filter = IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

                discoveryCancelReceiver = object : BroadcastReceiver() {
                    override fun onReceive(context: Context?, intent: Intent?) {
                        if (intent?.action == BluetoothAdapter.ACTION_DISCOVERY_FINISHED) {
                            Log.d("BluetoothConn", "Cancelación de descubrimiento terminada, iniciando conexión.")
                            try {
                                context?.unregisterReceiver(this)
                            } catch (e: IllegalArgumentException) {
                                // Ya desregistrado o contexto no válido, ignorar
                            }
                            discoveryCancelReceiver = null
                            connectSocket()
                        }
                    }
                }
                context.registerReceiver(discoveryCancelReceiver, filter)
                bluetoothAdapter.cancelDiscovery()
            } else {
                Log.d("BluetoothConn", "No hay descubrimiento activo, conectando directamente.")
                connectSocket()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun connectSocket() {
        try {
            socket = device.createRfcommSocketToServiceRecord(uuid)
            socket?.connect()

            outputStream = socket?.outputStream
            inputStream = socket?.inputStream

            Handler(Looper.getMainLooper()).post {
                onConnected?.invoke()
                notice.sendNotification(context, "OH SI!!", "Conexion Establecida!")
            }

            listenForMessages()

        } catch (e: IOException) {
            // Fallback con reflexión
            try {
                val m = device.javaClass.getMethod("createRfcommSocket", Int::class.javaPrimitiveType)
                socket = m.invoke(device, 1) as BluetoothSocket
                socket?.connect()

                outputStream = socket?.outputStream
                inputStream = socket?.inputStream

                Handler(Looper.getMainLooper()).post {
                    onConnected?.invoke()
                    notice.sendNotification(context, "OH SI!!", "Conexion Establecida!")
                }

                listenForMessages()

            } catch (inner: Exception) {
                socket?.close()
                socket = null
                Handler(Looper.getMainLooper()).post {
                    onConnectionFailed?.invoke(inner)
                }
            }
        }
    }

    fun sendMessage(message: String) {
        if (outputStream == null || socket == null || socket?.isConnected != true) {
            Log.e("BluetoothConn", "No se puede enviar: conexión no lista")
            return
        }
        try {
            outputStream?.write(message.toByteArray())
            outputStream?.flush()
        } catch (e: IOException) {
            Log.e("BluetoothConn", "Send failed: ${e.message}")
        }
    }

    private fun listenForMessages() {
        val buffer = ByteArray(1024)
        val stringBuilder = StringBuilder()
        isListening = true

        thread {
            try {
                while (isListening && socket?.isConnected == true) {
                    val bytesRead = inputStream?.read(buffer) ?: break
                    if (bytesRead > 0) {
                        val incoming = String(buffer, 0, bytesRead)
                        stringBuilder.append(incoming)

                        // Verifica si hay un salto de línea que indique fin de mensaje
                        var newlineIndex = stringBuilder.indexOf("\n")
                        while (newlineIndex != -1) {
                            val completeMessage = stringBuilder.substring(0, newlineIndex).trim()
                            if (completeMessage.isNotEmpty()) {
                                Log.d("BluetoothConn", "Mensaje completo recibido: $completeMessage")
                                onMessageReceived?.invoke(completeMessage)
                            }
                            // Remueve el mensaje ya procesado del buffer
                            stringBuilder.delete(0, newlineIndex + 1)
                            newlineIndex = stringBuilder.indexOf("\n")
                        }
                    } else if (bytesRead == -1) {
                        Log.w("BluetoothConn", "Socket cerrado por dispositivo remoto.")
                        break
                    }
                }
            } catch (e: IOException) {
                Log.e("BluetoothConn", "Error leyendo datos: ${e.message}")
            }

            Handler(Looper.getMainLooper()).post {
                onDisconnected?.invoke()
            }
        }
    }

    fun close() {
        isListening = false
        Log.d("BluetoothConn", "close() llamado")
        try {
            inputStream?.close()
        } catch (e: IOException) {
            Log.w("BluetoothConn", "Error al cerrar inputStream: ${e.message}")
        }

        try {
            outputStream?.close()
        } catch (e: IOException) {
            Log.w("BluetoothConn", "Error al cerrar outputStream: ${e.message}")
        }

        try {
            socket?.close()
            Log.d("BluetoothConn", "Socket cerrado correctamente")
        } catch (e: IOException) {
            Log.e("BluetoothConn", "Error al cerrar socket: ${e.message}")
        } finally {
            socket = null
            inputStream = null
            outputStream = null
        }

        // Asegurarse de desregistrar receiver si aún está activo
        if (discoveryCancelReceiver != null) {
            try {
                context.unregisterReceiver(discoveryCancelReceiver)
            } catch (e: IllegalArgumentException) {
                // Ya fue desregistrado o contexto inválido
            }
            discoveryCancelReceiver = null
        }
    }
}