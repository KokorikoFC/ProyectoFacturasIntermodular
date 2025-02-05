package com.example.proyectofacturasintermodular

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.example.proyectofacturasintermodular.ui.navigation.NavigationWrapper
import com.example.proyectofacturasintermodular.ui.theme.ProyectoFacturasIntermodularTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ProyectoFacturasIntermodularTheme {
                val navController = rememberNavController()
                val billRepository = BillRepository() // Instancia del repositorio

                NavigationWrapper(
                    navHostController = navController,
                    billRepository = billRepository
                )

            }
        }
    }
}