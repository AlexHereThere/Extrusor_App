package com.example.extrusor_interfaz_grafica

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothDevice
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.extrusor_interfaz_grafica.logica.BluetoothConnectionService
import com.example.extrusor_interfaz_grafica.logica.BluetoothHelper

class BluetoothViewModel(application: Application) : AndroidViewModel(application) {
    val bluetoothHelper = BluetoothHelper(application.applicationContext)
    private var connectionService: BluetoothConnectionService? = null
    private var receivedMessage by mutableStateOf("")
    val pairedDevices = bluetoothHelper.pairedDevices
    val discoveredDevices = bluetoothHelper.discoveredDevices
    var connection: BluetoothConnectionService? = null
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

    fun connectToDevice(device: BluetoothDevice, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        connection = BluetoothConnectionService(device,getApplication<Application>().applicationContext)

        connection?.onConnected = {
            connectionService = connection
            onSuccess()
        }

        connection?.onConnectionFailed = { e ->
            onFailure(e)
        }

        connection?.connect()
    }

    fun disconnect()
    {
        connection?.close()
    }
}