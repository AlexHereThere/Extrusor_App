package com.example.extrusor_interfaz_grafica.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.extrusor_interfaz_grafica.ui.theme.azulito


//Realmente tambien es una BottomBar pero pues ahi se va a navegar entonces pues
//Mejor navBar
@Composable
fun NavBar( iconoEncendido: Int) {

        val iconoDeSettings = remember { mutableStateOf(com.example.extrusor_interfaz_grafica.R.drawable.settingsnegro)}
        val iconoDeTermostato = remember { mutableStateOf(com.example.extrusor_interfaz_grafica.R.drawable.termostatonegro)}
        val iconoDeCronometro = remember { mutableStateOf(com.example.extrusor_interfaz_grafica.R.drawable.cronometronegro)}
        var iconoDeCheck  = remember { mutableStateOf(com.example.extrusor_interfaz_grafica.R.drawable.checknegro) }

        //Esta toda chafa la forma pero pues nimodo, no busque otra forma, si da lag ya sabemos porque
    if(iconoEncendido==0){
        iconoDeTermostato.value = com.example.extrusor_interfaz_grafica.R.drawable.termostatonegro //Deberia ser verde
        iconoDeSettings.value = com.example.extrusor_interfaz_grafica.R.drawable.settingsverde
        iconoDeCronometro.value = com.example.extrusor_interfaz_grafica.R.drawable.cronometrogris
        iconoDeCheck.value = com.example.extrusor_interfaz_grafica.R.drawable.checkgris
    }
    if(iconoEncendido==1){
        iconoDeTermostato.value = com.example.extrusor_interfaz_grafica.R.drawable.termostatonegro
        iconoDeSettings.value = com.example.extrusor_interfaz_grafica.R.drawable.settingsverde //Deberia ser verde
        iconoDeCronometro.value = com.example.extrusor_interfaz_grafica.R.drawable.cronometrogris
        iconoDeCheck.value = com.example.extrusor_interfaz_grafica.R.drawable.checkgris
    }
    if(iconoEncendido==2){
        iconoDeTermostato.value = com.example.extrusor_interfaz_grafica.R.drawable.termostatonegro
        iconoDeSettings.value = com.example.extrusor_interfaz_grafica.R.drawable.settingsnegro
        iconoDeCronometro.value = com.example.extrusor_interfaz_grafica.R.drawable.cronometroverde//Deberia ser verde
        iconoDeCheck.value = com.example.extrusor_interfaz_grafica.R.drawable.checkgris
    }
    if(iconoEncendido==3){
        iconoDeTermostato.value = com.example.extrusor_interfaz_grafica.R.drawable.termostatonegro //Deberia ser verde
        iconoDeSettings.value = com.example.extrusor_interfaz_grafica.R.drawable.settingsnegro
        iconoDeCronometro.value = com.example.extrusor_interfaz_grafica.R.drawable.cronometronegro
        iconoDeCheck.value = com.example.extrusor_interfaz_grafica.R.drawable.checkverde //Deberia ser verde
    }
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .padding(8.dp)
        ) {

            Image(
                painter = painterResource(id = iconoDeTermostato.value),
                contentDescription = "Logo",
                modifier = Modifier.size(64.dp)

            )
            Image(
                painter = painterResource(id = iconoDeSettings.value),
                contentDescription = "Logo",
                modifier = Modifier.size(64.dp)

            )
            Image(
                painter = painterResource(id = iconoDeCronometro.value),
                contentDescription = "Logo",
                modifier = Modifier.size(64.dp)

            )
            Image(
                painter = painterResource(id = iconoDeCheck.value),
                contentDescription = "Logo",
                modifier = Modifier.size(64.dp)

            )
        }
}