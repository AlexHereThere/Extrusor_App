package com.example.extrusor_interfaz_grafica.componentes

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.extrusor_interfaz_grafica.BluetoothViewModel

@Composable
fun TopBar(
    viewModel: BluetoothViewModel,
    navigationToDetail: ((String) -> Unit)?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 2.dp.toPx()
                )
            }) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(2.dp))
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                onClick = {
                    viewModel.disconnect()
                    navigationToDetail?.invoke("Login")
                          },
                modifier = Modifier.background(Color.Transparent)
            ) {
                Image(
                    painter = painterResource(id = com.example.extrusor_interfaz_grafica.R.drawable.bluetooth_discconect),
                    contentDescription = "Logo",
                    modifier = Modifier.size(72.dp)

                )
            }
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}