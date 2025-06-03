import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import android.app.Application
import android.os.Handler
import android.os.Looper
import com.example.extrusor_interfaz_grafica.logica.BluetoothConnectionService
import com.example.extrusor_interfaz_grafica.logica.BluetoothHelper


class BluetoothViewModel(application: Application) : AndroidViewModel(application) {

    val bluetoothHelper = BluetoothHelper(application.applicationContext)

    val lastReceivedMessage = mutableStateOf("")
    val pairedDevices = bluetoothHelper.pairedDevices
    val discoveredDevices = bluetoothHelper.discoveredDevices
    val currentTemperature = mutableStateOf("0")
    val isConnected = mutableStateOf(false)

    private var connectionService: BluetoothConnectionService? = null

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScan() {
        bluetoothHelper.startScanDiscovery()
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun loadPaired() {
        bluetoothHelper.loadPairedDevices()
    }

    fun registerReceivers() {
        bluetoothHelper.registerReceivers()
    }

    fun unregisterReceivers() {
        bluetoothHelper.unregisterReceivers()
    }

    fun connectToDevice(
        device: BluetoothDevice,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        // Cerrar conexión previa si existe
        connectionService?.close()

        connectionService = BluetoothConnectionService(device, getApplication<Application>().applicationContext)
        connectionService?.apply {
            onConnected = {
                Handler(Looper.getMainLooper()).post {
                    isConnected.value = true
                    onSuccess()
                }
            }
            onConnectionFailed = { e ->
                Handler(Looper.getMainLooper()).post {
                    isConnected.value = false
                    onFailure(e)
                }
            }
            onDisconnected = {
                Handler(Looper.getMainLooper()).post {
                    isConnected.value = false
                }
            }
            onMessageReceived = { message ->
                onBluetoothDataReceived(message)
            }
            connect()
        }
    }

    fun disconnect() {
        connectionService?.close()
        Handler(Looper.getMainLooper()).post {
            isConnected.value = false
        }
    }

    private fun onBluetoothDataReceived(data: String) {
        Handler(Looper.getMainLooper()).post {
            currentTemperature.value = data.trim()
            lastReceivedMessage.value = data.trim()
        }
    }

    fun sendMessage(message: String) {
        if (isConnected.value) {
            connectionService?.sendMessage(message)
        } else {
            android.util.Log.e("BluetoothViewModel", "No se puede enviar: conexión no lista")
        }
    }
}