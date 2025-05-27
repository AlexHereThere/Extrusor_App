package com.example.extrusor_interfaz_grafica.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.extrusor_interfaz_grafica.DetailScreen
import com.example.extrusor_interfaz_grafica.LoginScreen
import com.example.extrusor_interfaz_grafica.HomeScreen
import com.example.extrusor_interfaz_grafica.BluetoothViewModel

@Composable
fun NavigationWrapper(viewModel: BluetoothViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController , startDestination = Login){
        composable<Login> {
            LoginScreen(viewModel){navController.navigate(Home)}
        }
        composable<Home> {
            HomeScreen(viewModel){name -> navController.navigate(Detail(texto = name ))}
        }
        composable<Detail> { backStackEntry->
            var detail = backStackEntry.toRoute<Detail>()
            DetailScreen(detail.texto){navController.navigate(Login){
              popUpTo<Login>{inclusive = true} //Elimina la ruta de la pila
            } }
        }
    }
}