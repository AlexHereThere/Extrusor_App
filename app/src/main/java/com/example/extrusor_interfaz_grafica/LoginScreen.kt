package com.example.extrusor_interfaz_grafica

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.extrusor_interfaz_grafica.componentes.TopBar
import com.example.extrusor_interfaz_grafica.core.navigation.Login
import com.example.extrusor_interfaz_grafica.ui.theme.azulito
import com.example.extrusor_interfaz_grafica.popUp.Conectando
import com.example.extrusor_interfaz_grafica.ui.theme.azulito

//No sera realmente un login sera lo del bluetooh, estoy haciendo un tutorial pues
@SuppressLint("MissingPermission")
@Composable
fun LoginScreen(viewModel: BluetoothViewModel, navigateToHome: () -> Unit,navigateToLogin: () -> Unit) {
    viewModel.disconnect() //desconectarse para poder conectarse de nuevo.
    var pairedDevices = viewModel.pairedDevices
    var popUp = remember { mutableStateOf(false) }
    Conectando(showPopup = popUp.value, onDismiss = {
        popUp.value = false //Para que se cierre
    })
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,) {
        //Debe haber un if si no hay nada entoces poner un "NO hay nada :c "
        //Y si si hya pues se muestra, tambien debe tener como una barra superior, la barra superior no estoy seguro de que debe de tener
        TopBar(false,viewModel = viewModel ,navigationToLogin ={navigateToLogin()})
        if(pairedDevices.value.isEmpty()){
            Text("No hay bluetooth cercanos :c", fontSize = 60.sp, lineHeight = 50.sp, modifier = Modifier.padding(5.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)

        }else{
            ListaDeBluetooh(
                listaDeBluetooth = pairedDevices,
                navigateToHome = { dispositivo ->
                    //viewModel.connectToDevice(
                       // dispositivo,
                        //onSuccess = {
                            popUp.value = true
                            navigateToHome()
                        //},
                        //onFailure = { e ->
                        //   Log.e("Bluetooth", "No se pudo conectar: ${e.message}")
                        //    // Puedes mostrar un Toast o un mensaje de error aquí
                        //}
                    //)
                }
            )
        }
            Button(
                onClick = { viewModel.loadPaired() },//buscar mas dispositivos
                modifier = Modifier.fillMaxWidth().padding(32.dp)

            ) { Text("Buscar") }
    }

}

@SuppressLint("MissingPermission")
@Composable
private fun BloqueDeBluetooth(dispositivo: BluetoothDevice, tamano: Int, navigateToHome: (BluetoothDevice) -> Unit){

    Surface(
        onClick ={navigateToHome(dispositivo)},
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color(0xFF000000),
                shape = RoundedCornerShape(20.dp)
            )

            .padding(.5.dp)
            .size(width = 300.dp, height = 100.dp),
        color = azulito,
        shadowElevation = 40.dp,
        shape= RoundedCornerShape(20.dp),
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Text(text = dispositivo.name ?: "Sin nombre", fontSize = tamano.sp, color = Color.Black)
            Text(text = dispositivo.address, fontSize = 14.sp, color = Color.Magenta)
        }

    }
}
@Composable
private fun ListaDeBluetooh(listaDeBluetooth: MutableState<Set<BluetoothDevice>>, navigateToHome: (BluetoothDevice) -> Unit) {
    var tamano = 30
    LazyColumn(
        contentPadding = androidx.compose.foundation.layout.PaddingValues(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp),
        modifier = Modifier.fillMaxHeight(0.85F)
    ) {
        items(listaDeBluetooth.value.toList()) { dispositivo ->
            BloqueDeBluetooth(dispositivo, tamano, navigateToHome = {navigateToHome(dispositivo)})
        }
    }
}