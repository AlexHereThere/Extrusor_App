package com.example.extrusor_interfaz_grafica.popUp


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.border
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Popup
import com.example.extrusor_interfaz_grafica.ui.theme.azulito
import com.example.extrusor_interfaz_grafica.ui.theme.azulitoTransparente

@Composable
fun Conectando(showPopup: Boolean, onDismiss: () -> Unit) {
    if (showPopup) {
        Popup(
            onDismissRequest = onDismiss,
            alignment = androidx.compose.ui.Alignment.Center) {
            Surface(
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
                    .background(azulitoTransparente),
                shadowElevation = 8.dp
                    ,
                shape = RoundedCornerShape(12.dp),

                //tonalElevation = 8.dp
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .background(azulitoTransparente, RoundedCornerShape(12.dp))
                        .padding(6.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(48.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant)
                }
            }
    }
    }
}

