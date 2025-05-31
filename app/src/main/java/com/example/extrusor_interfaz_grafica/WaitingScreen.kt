package com.example.extrusor_interfaz_grafica


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.extrusor_interfaz_grafica.componentes.NavBar
import com.example.extrusor_interfaz_grafica.componentes.TopBar
import java.time.temporal.Temporal

@Composable
fun WaitingScreen(viewModel: BluetoothViewModel,navigateToLogin :() -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(true, viewModel=viewModel,navigationToLogin = {navigateToLogin()})
        Text("Filamento siendo extraido")
        imprimirResultados()


    }
}

@Composable
fun imprimirResultados(){
    //De Alguna forma se van a obtener pero solo para demostrarlos usare el remember
    var temperatura by remember { mutableStateOf("120") }
    var velocidad by remember { mutableStateOf("60")}
    var tiempo by remember { mutableStateOf("12:10")}

    Column(modifier = Modifier.padding(12.dp)) {
        Text("Temperatura con ${temperatura} C ")
        Text("Velocidad de ${velocidad} RPM ")
        Text("Tiempo de apagado ${tiempo}")
    }




}