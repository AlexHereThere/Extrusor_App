package com.example.extrusor_interfaz_grafica

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.extrusor_interfaz_grafica.componentes.TopBar
import com.example.extrusor_interfaz_grafica.ui.theme.azulito
import com.example.extrusor_interfaz_grafica.popUp.Conectando

//No sera realmente un login sera lo del bluetooh, estoy haciendo un tutorial pues
@Composable
fun LoginScreen(navigateToHome: () -> Unit) {
    var popUp = remember { mutableStateOf(false) }
    Conectando(showPopup = popUp.value, onDismiss = {
        popUp.value = false //Para que se cierre
        navigateToHome() //Para que se vaya al home, aunque solo es prueba este deberia cancelar la conexion bluetooth
    })
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,) {
        //Debe haber un if si no hay nada entoces poner un "NO hay nada :c "
        //Y si si hya pues se muestra, tambien debe tener como una barra superior, la barra superior no estoy seguro de que debe de tener
        TopBar()
        if(obtenerLista().isEmpty()){
            Text("No hay bluetooth cercanos :c", fontSize = 60.sp, lineHeight = 50.sp, modifier = Modifier.padding(5.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)

        }else{
            ListaDeBluetooh(
                listaDeBluetooth = obtenerLista(),
                navigateToHome = {
                    popUp.value = true
                    /*navigateToHome()*/
                    //Aqui deberia conectarse
                    //ConexionBluetooth(FE:qq:14:hh) Algo asi
                })
        }

    }
}

@Composable
private fun bloqueDeblluetooth(nombreDelBluetooh: String, tamano: Int, navigateToHome: () -> Unit){

    Surface(
        onClick ={navigateToHome()},
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
        Text(text = nombreDelBluetooh, fontSize = tamano.sp, modifier = Modifier.padding(10.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}
@Composable
private fun ListaDeBluetooh(listaDeBluetooth: List<String>, navigateToHome: () -> Unit) {
    var tamano = 30
    LazyColumn (contentPadding = androidx.compose.foundation.layout.PaddingValues(30.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)){
        items(listaDeBluetooth) {index->
            bloqueDeblluetooth(index,tamano,navigateToHome={navigateToHome()})
        }

    }
}

private fun obtenerLista() : List<String>{
    //var listaEpica:List<String> = listOf()
    var listaEpica:List<String> = listOf("Esp31","Esp32","Esp33","Esp34","Esp34","Esp34","Esp34","Esp34","Esp34")
    return listaEpica
}
@Composable
//Esta funcion es prueba solo para mostrar su funcionamiento
//Realmente aqui deberia haber una funcion que recolecte las mac de los bluetooth cercanos
//Y hacer una lista mostrarlos y al hacer clic pues se conectara
//se debe probar despues lo de que pasa si el nombre es grande
fun mostrarLalIstaBluetooth(navigateToHome: () -> Unit){
    var listaEpica:List<String> = listOf("Esp31","Esp32","Esp33","Esp34","Esp34","Esp34","Esp34","Esp34","Esp34")
    ListaDeBluetooh(listaDeBluetooth = listaEpica, navigateToHome = {navigateToHome()})
}