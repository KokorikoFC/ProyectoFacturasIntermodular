package com.example.proyectofacturasintermodular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.proyectofacturasintermodular.ui.navigation.NavigationWrapper
import com.example.proyectofacturasintermodular.ui.theme.ProyectoFacturasIntermodularTheme
import com.example.proyectofacturasintermodular.viewmodel.BillViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoFacturasIntermodularTheme {
                val navController = rememberNavController()
                val billViewModel = BillViewModel()

                NavigationWrapper(
                    navHostController = navController,
                    billViewModel = billViewModel
                )

            }
        }
    }
}