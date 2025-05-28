package com.example.extrusor_interfaz_grafica.logica

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import kotlin.concurrent.thread
import kotlin.text.toByteArray

class BluetoothConnectionService(
    private val device: BluetoothDevice,
    private val context: Context)
{

    private var notice: Notifier = Notifier()
    @Volatile
    private var isListening = false
    private val uuid: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private var socket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null
    private var inputStream: InputStream? = null
    var onConnected: (() -> Unit)? = null
    var onConnectionFailed: ((Exception) -> Unit)? = null
    var onMessageReceived: ((String) -> Unit)? = null

    //...

    @SuppressLint("MissingPermission")
    fun connect() {
        thread {
            try {
                socket = device.createRfcommSocketToServiceRecord(uuid)
                socket?.connect()

                outputStream = socket?.outputStream
                inputStream = socket?.inputStream

                Handler(Looper.getMainLooper()).post {
                    onConnected?.invoke()
                    notice.sendNotification(context,"OH SI!!","Conexion Establecida!")
                }

                listenForMessages()
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    onConnectionFailed?.invoke(e)
                }
            }
        }
    }

    fun sendMessage(message: String) {
        try {
            outputStream?.write(message.toByteArray())
        } catch (e: Exception) {
            Log.e("BluetoothConn", "Send failed: ${e.message}")
        }
    }

    private fun listenForMessages() {
        val buffer = ByteArray(1024)
        isListening = true
        while (isListening) {
            try {
                val bytes = inputStream?.read(buffer) ?: break
                if (bytes == -1) {
                    Log.i("BluetoothConn", "Socket cerrado, saliendo del loop de lectura")
                    break
                }
                val message = String(buffer, 0, bytes)
                onMessageReceived?.invoke(message)
            } catch (e: Exception) {
                // Manejo específico para socket cerrado
                if (e.message?.contains("socket closed", ignoreCase = true) == true) {
                    Log.i("BluetoothConn", "Socket cerrado detectado en catch, finalizando lectura")
                } else {
                    Log.e("BluetoothConn", "Receive failed: ${e.message}")
                }
                break
            }
        }
        isListening = false
    }

    fun close() {
        isListening = false
        try {
            socket?.close()
            socket = null
        } catch (_: Exception) {}
    }
}