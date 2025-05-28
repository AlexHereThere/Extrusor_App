package com.example.extrusor_interfaz_grafica.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.extrusor_interfaz_grafica.DetailScreen
import com.example.extrusor_interfaz_grafica.LoginScreen
import com.example.extrusor_interfaz_grafica.HomeScreen
import com.example.extrusor_interfaz_grafica.WaitingScreen

@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    NavHost(navController = navController , startDestination = Login){
        composable<Login> {
            LoginScreen( navigateToHome = { navController.navigate(Home)}, navigateToLogin = {
                navController.navigate(Login){
                    popUpTo<Login>{inclusive = true}
                }
            })
        }
        composable<Home> {
            HomeScreen(navigateToWaiting = {navController.navigate(Waiting)}, navigateToLogin = {                navController.navigate(Login){
                popUpTo<Login>{inclusive = true}
            }})
        }
        composable<Waiting> {
            WaitingScreen{navController.navigate(Login){
                popUpTo<Login>{inclusive = true}
            } }
        }
        composable<Detail> { backStackEntry->
            var detail = backStackEntry.toRoute<Detail>()
            DetailScreen(detail.texto){navController.navigate(Login){
              popUpTo<Login>{inclusive = true} //Elimina la ruta de la pila
            } }
        }
    }
}