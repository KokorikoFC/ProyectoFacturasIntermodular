package com.example.proyectofacturasintermodular.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.proyectofacturasintermodular.ui.AddBill
import com.example.proyectofacturasintermodular.viewmodel.BillViewModel

@Composable
fun NavigationWrapper(navHostController: NavHostController, billViewModel: BillViewModel) {
    NavHost(navController = navHostController, startDestination = "addBill") {
        composable("addBill") { AddBill(navHostController, billViewModel) }
    }
}


