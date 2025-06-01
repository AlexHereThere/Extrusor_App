package com.example.extrusor_interfaz_grafica



import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.extrusor_interfaz_grafica.componentes.TopBar

@Composable
fun HomeScreen(viewModel: BluetoothViewModel,navigateToWaiting :() -> Unit,navigateToLogin :() -> Unit) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        TopBar(true, viewModel = viewModel, navigationToLogin = {navigateToLogin()})
        /*
            Contenido Epico
         */
        FormularioYContenido()
    }


}

//Aqui se va a enviar por bluetooh, es decir aqui hay como un formulario, lo que esta aqui es
// Text field para que se pueda leer esto
@Composable
fun FormularioYContenido() {
    var temperatura by remember { mutableStateOf("") }
    var tiempo by remember { mutableStateOf("") }
    var tempActual by remember { mutableStateOf("0") }
    var tiempoActual by remember { mutableStateOf("0:0:0") }
    var screenState by remember { mutableIntStateOf(0) } // si no viene de fuera
    val textoBoton = when (screenState) {
        0 -> "Listo"
        1 -> "Empezar"
        else -> "Detener"
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Ingresar parámetros al extractor PET")
        CustomtextField(
            value = temperatura,
            onValueChange = { if (it.length < 4) temperatura = it },
            label = "Temperatura (C)"
        )
        Spacer(modifier = Modifier.height(8.dp))
        CustomtextField(
            value = tiempo,
            onValueChange = { if (it.length < 4) tiempo = it },
            label = "Tiempo (Minutos)"
        )
        Spacer(modifier = Modifier.height(16.dp))
        if(screenState==1) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.8f)

            ) { Text("Cambiar") }
        }
    }


        Column(modifier = Modifier
            .fillMaxHeight(0.50F),
            verticalArrangement = Arrangement.Top) {
            Text(
                fontSize = 20.sp,
                text = "Temperatura: $tempActual",

                )
            Spacer(Modifier.height(12.dp))
            Text(
                fontSize = 20.sp,
                text = "Tiempo: $tiempoActual",

                )

            GifView(R.drawable.engranaje_gif, modifier = Modifier)
        }

    //boton inferior
    Column(
        modifier = Modifier
            .padding(32.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                screenState = if (screenState < 2) screenState + 1 else 0
            }
        ) {
            AnimatedContent(
                targetState = textoBoton,
                label = "Button Text Animation"
            ) { text ->
                Text(text = text)
            }
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


@Composable
fun GifView(resourceId: Int, modifier: Modifier = Modifier) {
    val context = LocalContext.current

    // Cargador con soporte para GIFs
    val imageLoader = coil.ImageLoader.Builder(context)
        .components {
            add(GifDecoder.Factory())
        }
        .build()

    AsyncImage(
        model = ImageRequest.Builder(context)
            .data(resourceId) // Usa el ID de recurso, no una URL
            .build(),
        contentDescription = "GIF animado",
        imageLoader = imageLoader,
        modifier = modifier
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

