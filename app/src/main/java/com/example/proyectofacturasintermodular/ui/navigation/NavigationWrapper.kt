package com.example.proyectofacturasintermodular.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.example.proyectofacturasintermodular.ui.AddBill

@Composable
fun NavigationWrapper(navHostController: NavHostController, billRepository: BillRepository) {
    NavHost(navController = navHostController, startDestination = "addBill") {
        composable("addBill") { AddBill(navHostController, billRepository) }
    }
}


