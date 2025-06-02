package com.example.extrusor_interfaz_grafica


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.request.ImageRequest
import com.example.extrusor_interfaz_grafica.componentes.TopBar
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun HomeScreen(
    viewModel: BluetoothViewModel,
    navigateToWaiting: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    var text by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(true, viewModel = viewModel, navigationToLogin = { navigateToLogin() })
        /*
            Contenido Epico
         */

        //Prueba de reloj
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center){
                RelojDeCarga(
                    totalTime = 100,
                    handleColor = Color.Green,
                    inactiveBarColor = Color.Cyan,
                    activeBar = Color.Yellow,
                    modifer = Modifier.size(200.dp),
                )

            }
        }
    //Quitado para probar el reloj
    //FormularioYContenido()
    }


}
//Aqui se va a enviar por bluetooh, es decir aqui hay como un formulario, lo que esta aqui es
// Text field para que se pueda leer esto

//No me parece correcto que tengamos un componente formulario y contenido
//Para mostrarlo creo que deberia ser mejor algo asi
/*
*  If(RespuestaBluetooth == "EstoyCalentando"){
*   mostrarContenidoDeMoitoreio(); //Waiting Screen
* }else{
*   mostrarFormulario(); //HomeScreen
* }
*
* */
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
        if (screenState == 1) {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth(0.8f)

            ) { Text("Cambiar") }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxHeight(0.50F),
        verticalArrangement = Arrangement.Top
    ) {
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
    onValueChange: (String) -> Unit,
) {
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




@Composable
fun RelojDeCarga(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBar: Color,
    modifer: Modifier = Modifier,
    initialValue: Float = 0f,
    strokeWidth: Dp = 5.dp,
) {

    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableStateOf(initialValue) }
    var currentTime by remember { mutableStateOf(totalTime) }
    var isTimerRunning by remember { mutableStateOf(false) }

    //Cuando cambie alguna de las key se hara lo de adentro
    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning ) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L) //100 milisegundos
            currentTime -= 100L
            value = currentTime / totalTime.toFloat()
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .onSizeChanged {
                size = it
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = inactiveBarColor,
                startAngle = -215f,
                sweepAngle = 250f,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )

            drawArc(
                color = activeBar,
                startAngle = -215f,
                sweepAngle = 250f * value,
                useCenter = false,
                size = Size(size.width.toFloat(), size.height.toFloat()),
                style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)

            )
            val center = Offset(size.width / 2f, size.height / 2f);
            val beta = ((250f * value) + 145f) * (PI / 180f).toFloat()
            val r = size.width / 2
            val a = cos(beta) * r
            val b = sin(beta) * r

            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),

                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }
        Text(
            text = (currentTime / 1000L).toString(),
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Button(
            onClick = {
                if (currentTime <= 0L) {
                    currentTime = totalTime
                    isTimerRunning = true
                } else {
                    isTimerRunning = !isTimerRunning
                }
    },
    modifier = Modifier.align(Alignment.BottomCenter),
    colors = ButtonDefaults.buttonColors(
        containerColor = if (isTimerRunning || currentTime <= 0L) {
            Color.Green
        } else {
            Color.Red
        }
    ),
    enabled = TODO(),
    shape = TODO(),
    elevation = TODO(),
    border = TODO(),
    contentPadding = TODO(),
    interactionSource = TODO(),
    content = TODO()
    )
    Text(
        text = if (isTimerRunning && currentTime >= 0L) "stop"
        else if (!isTimerRunning && currentTime >= 0L) "start"
        else "Restart"
    )
}
}


//Metodo Debug para mostrar el reloj que estoy haciendo
@Composable
@Preview
fun mostrarReloj() {

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

