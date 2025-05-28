package com.example.extrusor_interfaz_grafica

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.extrusor_interfaz_grafica.core.navigation.NavigationWrapper
import com.example.extrusor_interfaz_grafica.ui.theme.Extrusor_Interfaz_GraficaTheme

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    private val bluetoothViewModel: BluetoothViewModel by viewModels()
    private val permissionCode = 1
    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            Log.i("Bluetooth", "Bluetooth enabled")
        } else {
            Log.i("Bluetooth", "Bluetooth not enabled")
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private fun requestBluetoothPermission() {
        val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        activityResultLauncher.launch(enableBluetoothIntent)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkAndRequestPermissions()
        enableEdgeToEdge()
        setContent {
            Extrusor_Interfaz_GraficaTheme {
                NavigationWrapper(viewModel = bluetoothViewModel)
            }
        }
    }

    private fun checkAndRequestPermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
            if (permissions.any {
                    ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
                }) {
                ActivityCompat.requestPermissions(this, permissions, permissionCode)
                return
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    permissionCode
                )
                return
            }
        }

        // Solicita encender el Bluetooth si no está habilitado
        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        if (!bluetoothAdapter.isEnabled &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        ) {
            requestBluetoothPermission()
        }

        // Llama al ViewModel para que registre los receivers y cargue dispositivos emparejados
        bluetoothViewModel.registerReceivers()
        bluetoothViewModel.loadPaired()
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetoothViewModel.unregisterReceivers()
    }
}