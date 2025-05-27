package com.example.extrusor_interfaz_grafica


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.extrusor_interfaz_grafica.componentes.NavBar
import com.example.extrusor_interfaz_grafica.componentes.TopBar

@Composable
fun HomeScreen(viewModel: BluetoothViewModel,navigationToDetail :(String) -> Unit) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(viewModel,navigationToDetail)
        /*
            Contenido Epico
         */

    }
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    )
    {NavBar()}


    }
/*
        NavBar()
        Spacer(modifier = Modifier.weight(1f))
    }*/

/*
            Spacer(modifier = Modifier.weight(1f))
        Text("Hola soy el home", fontSize = 30.sp)
        Spacer(modifier = Modifier.weight(1f))
        TextField(value = text, onValueChange = { text = it })
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { navigationToDetail(text) }) {
            Text("Navegar al Login")
        }
         */