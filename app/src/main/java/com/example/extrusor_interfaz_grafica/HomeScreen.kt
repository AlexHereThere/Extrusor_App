package com.example.extrusor_interfaz_grafica

import BluetoothViewModel
import android.annotation.SuppressLint
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopBar(true, viewModel = viewModel, navigationToLogin = { navigateToLogin() })

    FormularioYContenido(viewModel)
    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioYContenido(viewModel: BluetoothViewModel) {
    var temperatura by remember { mutableStateOf("100") }
    var tiempoMinutos by remember { mutableStateOf("10") }
    var tempActual by remember { mutableStateOf("0") }
    var screenState by remember { mutableIntStateOf(0) }


    // Timer control
    val tiempoEnSegundos = (tiempoMinutos.toLongOrNull() ?: 1L) * 60
    var currentTime by remember { mutableLongStateOf(0L) }
    var isTimerRunning by remember { mutableStateOf(false) }

    val textoBoton = when (screenState) {
        0 -> "Listo"
        1 -> "Empezar"
        else -> "Detener"
    }

    LaunchedEffect(screenState) {
        if(screenState == 2)
        {
           updateArduino(
                viewModel = viewModel,
                tempRef = temperatura,
                timeSeg = tiempoEnSegundos.toString(),
                startMove = "1", // mover el motor
                startTimer = "1" // el temporizador ha empezado
           )
        }
        else{
            currentTime = tiempoEnSegundos
            updateArduino(
                viewModel = viewModel,
                tempRef = temperatura,
                timeSeg = tiempoEnSegundos.toString(),
                startMove = "0", // no quieres mover el motor todavía
                startTimer = "0" // el temporizador aún no ha empezado
            )
        }

    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (screenState == 0) {
            // Formulario
            Text("Ingresar parámetros al extractor PET")
            CustomtextField(
                value = temperatura,
                onValueChange = { if (it.length < 4) temperatura = it },
                label = "Temperatura (C)"
            )
            Spacer(modifier = Modifier.height(8.dp))
            CustomtextField(
                value = tiempoMinutos,
                onValueChange = { if (it.length < 4) tiempoMinutos = it },
                label = "Tiempo (Minutos)"
            )
        } else {

            // Pantalla de monitoreo con reloj
            tempActual = viewModel.currentTemperature.value
            Text("Temperatura Actual")
            TextField(value = tempActual, onValueChange = {}, readOnly = true)
            Text("Temperatura Deseada")
            TextField(value = temperatura, onValueChange = {}, readOnly = true)
            if(screenState==1)
            {
                Text( modifier = Modifier.fillMaxWidth(0.8F), text = "Espere hasta llegar a temperatura deseada.")
            }else if(screenState==2)
            {
                Text( modifier = Modifier.fillMaxWidth(0.8F), text = "Hay que esperar...")
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.8F)
                    .fillMaxHeight(0.7F)
                    .padding(horizontal = 5.dp, vertical = 5.dp),
                color = Color.Transparent
            ) {
                Box(contentAlignment = Alignment.Center) {
                    RelojDeCarga(
                        totalTime = tiempoEnSegundos,
                        handleColor = Color.Green,
                        inactiveBarColor = Color.DarkGray,
                        activeBar = Color.Red,
                        isTimerRunning = isTimerRunning,
                        currentTime = currentTime,
                        onTick = { currentTime = it },
                        onFinished = {
                            isTimerRunning = false
                        }
                    )
                }
            }
        }
    }

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
                when (screenState) {
                    0 -> screenState = 1 // Avanza al estado "Empezar"
                    1 -> {
                        // Inicia el timer
                        isTimerRunning = true
                        screenState = 2
                    }
                    2 -> {
                        isTimerRunning = false
                        screenState = 0
                        currentTime = tiempoEnSegundos // ✅ Reinicia correctamente
                    }
                }
            }
        ) {
            AnimatedContent(targetState = textoBoton, label = "Botón animado") { text ->
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
        label = { Text(text = label,
            color = Color.Black) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White
    )
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

@SuppressLint("DefaultLocale")
@Composable
fun RelojDeCarga(
    totalTime: Long,
    handleColor: Color,
    inactiveBarColor: Color,
    activeBar: Color,
    modifer: Modifier = Modifier,
    strokeWidth: Dp = 5.dp,
    isTimerRunning: Boolean,
    currentTime: Long,
    onTick: (Long) -> Unit,
    onFinished: () -> Unit
) {
    var size by remember { mutableStateOf(IntSize.Zero) }
    var value by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(key1 = currentTime, key2 = totalTime) {
        value = (currentTime.toFloat() / totalTime).coerceIn(0f, 1f)
    }

    LaunchedEffect(key1 = isTimerRunning, key2 = currentTime) {
        if (isTimerRunning && currentTime > 0L) {
            delay(1000L)
            onTick(currentTime - 1)
        }
        if (isTimerRunning && currentTime <= 0L) {
            onFinished()
        }
    }
    Box(contentAlignment = Alignment.Center, modifier = Modifier.onSizeChanged { size = it }) {
        Canvas(modifier = Modifier.size(250.dp)) {
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
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.width / 2f

            val sweep = 250f
            val startAngle = -215f
            val angle = startAngle + (sweep * value) // NOTA: 1 - value para invertir
            val beta = angle * (PI / 180f).toFloat()

            val a = cos(beta) * radius
            val b = sin(beta) * radius

            drawPoints(
                listOf(Offset(center.x + a, center.y + b)),
                pointMode = PointMode.Points,
                color = handleColor,
                strokeWidth = (strokeWidth * 3f).toPx(),
                cap = StrokeCap.Round
            )
        }
        val minutes = (currentTime / 1L) / 60
        val seconds = (currentTime / 1L) % 60
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        Text(


            text = formattedTime,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

fun updateArduino(viewModel: BluetoothViewModel, tempRef: String,timeSeg: String,startMove: String,startTimer: String)
{
    val minutes = (timeSeg.toInt() / 1L) / 60
    val seconds = (timeSeg.toInt() / 1L) % 60
    val str = "$tempRef,$minutes,$seconds,$startMove,$startTimer"
    viewModel.sendMessage(str)
}



