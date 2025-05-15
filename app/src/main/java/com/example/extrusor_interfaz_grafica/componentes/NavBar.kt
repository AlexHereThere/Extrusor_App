package com.example.extrusor_interfaz_grafica.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


//Realmente tambien es una BottomBar pero pues ahi se va a navegar entonces pues
//Mejor navBar
@Composable
fun NavBar() {


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .padding(horizontal = 10.dp)
            .fillMaxWidth()
            .background(Color.LightGray)
            .padding(8.dp)
    ) {

        Image(
            painter = painterResource(id = com.example.extrusor_interfaz_grafica.R.drawable.device_thermostat_64dp_000000_fill0_wght400_grad0_opsz48),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp)

        )
        Image(
            painter = painterResource(id = com.example.extrusor_interfaz_grafica.R.drawable.settings_64dp_000000_fill0_wght400_grad0_opsz48),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp)

        )
        Image(
            painter = painterResource(id = com.example.extrusor_interfaz_grafica.R.drawable.schedule_64dp_000000_fill0_wght400_grad0_opsz48),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp)

        )
        Image(
            painter = painterResource(id = com.example.extrusor_interfaz_grafica.R.drawable.check_circle_64dp_000000_fill0_wght400_grad0_opsz48),
            contentDescription = "Logo",
            modifier = Modifier.size(64.dp)

        )
    }
}