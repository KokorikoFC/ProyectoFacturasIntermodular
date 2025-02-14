package com.example.proyectofacturasintermodular.ui.navigation

import LoginScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofacturasintermodular.ui.AddBill
import com.example.proyectofacturasintermodular.viewmodel.*

@Composable
fun NavigationWrapper(navHostController: NavHostController, billViewModel: BillViewModel,authViewModel:AuthViewModel) {
    NavHost(navController = navHostController, startDestination = "addBill") {
        composable("loginScreen") { LoginScreen(navHostController, authViewModel) }
        composable("addBill") { AddBill(navHostController, billViewModel) }
    }
}






