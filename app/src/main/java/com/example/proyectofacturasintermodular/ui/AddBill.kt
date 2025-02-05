package com.example.proyectofacturasintermodular.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddBill(navHostController: NavHostController, billRepository: BillRepository) {
    var isIssued by remember { mutableStateOf(true) }
    var isIssuerExpanded by remember { mutableStateOf(false) }
    var isReceiverExpanded by remember { mutableStateOf(false) }
    var isAmountsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Registrar factura", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))

        // Toggle Emitida / Recibida
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray, RoundedCornerShape(8.dp))
                .padding(4.dp)
        ) {
            Button(
                onClick = { isIssued = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIssued) Color.Blue else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Emitida", color = Color.White)
            }

            Button(
                onClick = { isIssued = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isIssued) Color.Blue else Color.Gray
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text("Recibida", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Número de factura
        OutlinedTextField(
            value = "",
            onValueChange = {},
            label = { Text("Nº Factura") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Datos del emisor
        ExpandableSection(
            title = "Datos del emisor",
            isExpanded = isIssuerExpanded,
            onToggle = { isIssuerExpanded = !isIssuerExpanded }
        ) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Datos del receptor
        ExpandableSection(
            title = "Datos del receptor",
            isExpanded = isReceiverExpanded,
            onToggle = { isReceiverExpanded = !isReceiverExpanded }
        ) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Importes
        ExpandableSection(
            title = "Importes",
            isExpanded = isAmountsExpanded,
            onToggle = { isAmountsExpanded = !isAmountsExpanded }
        ) {
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Base Imponible") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("IVA") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("IRPF") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = "", onValueChange = {}, label = { Text("Total") }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Registrar
        Button(
            onClick = { /* Acción de registrar */ },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registrar", fontSize = 16.sp)
        }
    }
}

// Componente para secciones plegables
@Composable
fun ExpandableSection(title: String, isExpanded: Boolean, onToggle: () -> Unit, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(8.dp),
        ) {
            Text(title, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        if (isExpanded) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}