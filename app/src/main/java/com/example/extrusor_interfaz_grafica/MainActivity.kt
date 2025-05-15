package com.example.extrusor_interfaz_grafica

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.extrusor_interfaz_grafica.core.navigation.NavigationWrapper
import com.example.extrusor_interfaz_grafica.ui.theme.Extrusor_Interfaz_GraficaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Extrusor_Interfaz_GraficaTheme {
                NavigationWrapper()
            }
        }
    }
}

