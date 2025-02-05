package com.example.proyectofacturasintermodular.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofacturasintermodular.data.model.Bill
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.example.proyectofacturasintermodular.viewmodel.BillViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddBill(navHostController: NavHostController, billViewModel: BillViewModel) {

    var isIssued by remember { mutableStateOf(true) }
    var numeroFactura by remember { mutableStateOf("") }
    var nombreEmisor by remember { mutableStateOf("") }
    var nifEmisor by remember { mutableStateOf("") }
    var direccionEmisor by remember { mutableStateOf("") }
    var nombreReceptor by remember { mutableStateOf("") }
    var nifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }
    var baseImponible by remember { mutableStateOf("") }
    var iva by remember { mutableStateOf("") }
    var irpf by remember { mutableStateOf("") }
    var total by remember { mutableStateOf("") }

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
            value = numeroFactura,
            onValueChange = { numeroFactura = it },
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
            OutlinedTextField(value = nombreEmisor, onValueChange = { nombreEmisor = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = nifEmisor, onValueChange = { nifEmisor = it }, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccionEmisor, onValueChange = { direccionEmisor = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Datos del receptor
        ExpandableSection(
            title = "Datos del receptor",
            isExpanded = isReceiverExpanded,
            onToggle = { isReceiverExpanded = !isReceiverExpanded }
        ) {
            OutlinedTextField(value = nombreReceptor, onValueChange = { nombreReceptor = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = nifReceptor, onValueChange = { nifReceptor = it }, label = { Text("NIF") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = direccionReceptor, onValueChange = { direccionReceptor = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Importes
        ExpandableSection(
            title = "Importes",
            isExpanded = isAmountsExpanded,
            onToggle = { isAmountsExpanded = !isAmountsExpanded }
        ) {
            OutlinedTextField(value = baseImponible, onValueChange = { baseImponible = it }, label = { Text("Base Imponible") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = iva, onValueChange = { iva = it }, label = { Text("IVA") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = irpf, onValueChange = { irpf = it }, label = { Text("IRPF") }, modifier = Modifier.fillMaxWidth())
            Text("Total: ${billViewModel.calculateTotal(baseImponible, iva, irpf)}", fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Registrar
        Button(
            onClick = {
                val fechaEmision = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                val bill = Bill(
                    numeroFactura = numeroFactura,
                    fechaEmision = fechaEmision,
                    empresaEmisor = nombreEmisor,
                    nifEmisor = nifEmisor,
                    direccionEmisor = direccionEmisor,
                    clienteReceptor = nombreReceptor,
                    nifReceptor = nifReceptor,
                    direccionReceptor = direccionReceptor,
                    baseImponible = baseImponible.toDoubleOrNull() ?: 0.0,
                    iva = iva.toDoubleOrNull() ?: 0.0,
                    irpf = irpf.toDoubleOrNull() ?: 0.0,
                    total = total.toDoubleOrNull() ?: 0.0,
                    esFacturaEmitida = isIssued
                )
                billViewModel.addBill(bill)
            },
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