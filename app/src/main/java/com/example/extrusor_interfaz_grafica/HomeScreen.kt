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
fun HomeScreen(navigateToWaiting :() -> Unit,navigateToLogin :() -> Unit ) {
    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(true, navigationToLogin ={navigateToLogin()})

        Formulario{navigateToWaiting()}

    }


}

//Aqui se va a enviar por bluetooh, es decir aqui hay como un formulario, lo que esta aqui es
// Text field para que se pueda leer esto
@Composable
fun Formulario(navigationToWaiting :() -> Unit) {
    var temperatura by remember { mutableStateOf("") }
    var velocidad by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ingresar parametros al extractor PET")
        CustomtextField(
            value = temperatura,
            onValueChange = { if (it.length < 4) temperatura = it },
            label = "Temperatura (C)"
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomtextField(
            value = velocidad,
            onValueChange = { if (it.length < 4) velocidad = it },
            label = "Velocidad(RPM)"
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomtextField(
            value = tiempo,
            onValueChange = { if (it.length < 4) tiempo = it },
            label = "Tiempo (Minutos)"
        )
    }
    Column(modifier = Modifier.padding(32.dp).fillMaxSize(), Arrangement.Bottom) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(modifier = Modifier.fillMaxWidth(), onClick = {navigationToWaiting() }) {
            Text("Siguiente")
        }
    }
}

@Composable
fun CustomtextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
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