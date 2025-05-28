package com.example.extrusor_interfaz_grafica.logica

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.mutableStateOf
import kotlin.apply
import kotlin.collections.plus
import kotlin.collections.toSet
import kotlin.jvm.java
import kotlin.let

class BluetoothHelper(
    private val context: Context,
) {

    private val bluetoothManager =
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    private val bluetoothLeScanner: BluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner

    var pairedDevices = mutableStateOf(setOf<BluetoothDevice>())
    var discoveredDevices = mutableStateOf(setOf<BluetoothDevice>())

    private var isReceiverRegistered = false

    private val receiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onReceive(ctx: Context?, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? =
                        intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE, BluetoothDevice::class.java)
                    device?.let {
                        discoveredDevices.value = discoveredDevices.value + it
                    }
                    Log.i("Bluetooth", "Device found: ${device?.name}")
                }

                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> {
                    Log.i("Bluetooth", "Discovery started")
                }

                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    Log.i("Bluetooth", "Discovery finished")
                }
            }
        }
    }

    fun registerReceivers() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
            }
            context.registerReceiver(receiver, filter)
            isReceiverRegistered = true
        }
    }

    fun unregisterReceivers() {
        if (isReceiverRegistered) {
            try {
                context.unregisterReceiver(receiver)
            } catch (e: IllegalArgumentException) {
                Log.w("BluetoothHelper", "Receiver no estaba registrado")
            } finally {
                isReceiverRegistered = false
            }
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun startScanDiscovery() {
        val scanCallback = object : ScanCallback() {
            @SuppressLint("MissingPermission")
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                result?.device?.let { device ->
                    discoveredDevices.value = discoveredDevices.value + device
                    Log.i("Bluetooth", "BLE Device found: ${device.name}")
                }
            }

            override fun onScanFailed(errorCode: Int) {
                Log.e("Bluetooth", "Scan failed with error code: $errorCode")
            }
        }

        bluetoothLeScanner.startScan(scanCallback)

        Handler(Looper.getMainLooper()).postDelayed({
            bluetoothLeScanner.stopScan(scanCallback)
            Log.i("Bluetooth", "BLE scan stopped")
        }, 10000L)
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun loadPairedDevices() {
        pairedDevices.value = bluetoothAdapter.bondedDevices.toSet()
    }
}