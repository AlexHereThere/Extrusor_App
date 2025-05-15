package com.example.extrusor_interfaz_grafica.core.navigation

import kotlinx.serialization.Serializable

//No sera login real ser el bluetotoh
@Serializable
object Login

@Serializable
object Home

@Serializable
data class Detail(val texto: String)