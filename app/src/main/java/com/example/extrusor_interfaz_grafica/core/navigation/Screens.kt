package com.example.extrusor_interfaz_grafica.core.navigation

import kotlinx.serialization.Serializable

//No sera login real ser el bluetotoh
@Serializable
object Login
//Aqui estara nuestro formulario epico
@Serializable
object Home
//Aqui estara como nuestra screen en espera, si el
//Si el sistema ya esta sacando el filamento entonces mandara aqui
@Serializable
object Waiting
@Serializable
data class Detail(val texto: String)