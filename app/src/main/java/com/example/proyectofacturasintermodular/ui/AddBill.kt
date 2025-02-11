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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.proyectofacturasintermodular.data.model.Bill
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.example.proyectofacturasintermodular.viewmodel.BillViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
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
    var irpf by remember { mutableStateOf("") }

    // Estado para el tipo de IVA seleccionado en el Dropdown. Inicializado al primer tipo (General)
    var ivaSeleccionadoState by remember { mutableStateOf(billViewModel.tiposIVA[0]) }

    // Cálculo del total, ahora pasando el tipo de IVA seleccionado
    var total by remember(baseImponible, irpf, ivaSeleccionadoState) {
        mutableStateOf(billViewModel.calculateTotal(baseImponible, irpf, ivaSeleccionadoState))
    }


    var isIssuerExpanded by remember { mutableStateOf(false) }
    var isReceiverExpanded by remember { mutableStateOf(false) }
    var isAmountsExpanded by remember { mutableStateOf(false) }
    var expandedIVA by remember { mutableStateOf(false) } // Para el Dropdown del IVA


    LaunchedEffect(Unit) {
        numeroFactura = billViewModel.generarNumeroFactura()
    }


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("Añadir Factura", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Toggle Emitida / Recibida (sin cambios)
        item {
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
        }

        // Número de factura (sin cambios)
        item {
            Text("ID Factura: $numeroFactura", fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
        }

        // Datos del emisor (sin cambios)
        item {
            ExpandableSection(
                title = "Datos del emisor",
                isExpanded = isIssuerExpanded,
                onToggle = { isIssuerExpanded = !isIssuerExpanded }
            ) {
                OutlinedTextField(
                    value = nombreEmisor,
                    onValueChange = { nombreEmisor = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nifEmisor,
                    onValueChange = { nifEmisor = it },
                    label = { Text("NIF") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = direccionEmisor,
                    onValueChange = { direccionEmisor = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Datos del receptor (sin cambios)
        item {
            ExpandableSection(
                title = "Datos del receptor",
                isExpanded = isReceiverExpanded,
                onToggle = { isReceiverExpanded = !isReceiverExpanded }
            ) {
                OutlinedTextField(
                    value = nombreReceptor,
                    onValueChange = { nombreReceptor = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = nifReceptor,
                    onValueChange = { nifReceptor = it },
                    label = { Text("NIF") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = direccionReceptor,
                    onValueChange = { direccionReceptor = it },
                    label = { Text("Dirección") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Importes (Modificado para Dropdown de IVA)
        item {
            ExpandableSection(
                title = "Importes",
                isExpanded = isAmountsExpanded,
                onToggle = { isAmountsExpanded = !isAmountsExpanded }
            ) {
                OutlinedTextField(
                    value = baseImponible,
                    onValueChange = { baseImponible = it },
                    label = { Text("Base Imponible") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                // Dropdown para seleccionar el tipo de IVA
                ExposedDropdownMenuBox(
                    expanded = expandedIVA,
                    onExpandedChange = { expandedIVA = !expandedIVA }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = ivaSeleccionadoState.nombre, // Muestra el nombre del IVA seleccionado
                        onValueChange = { },
                        label = { Text("Tipo IVA") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expandedIVA
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expandedIVA,
                        onDismissRequest = { expandedIVA = false }
                    ) {
                        billViewModel.tiposIVA.forEach { tipoIVA ->
                            DropdownMenuItem(
                                text = { Text(text = "${tipoIVA.nombre} (${tipoIVA.porcentaje}%)") },
                                onClick = {
                                    ivaSeleccionadoState = tipoIVA // Actualiza el estado del IVA seleccionado
                                    billViewModel.actualizarIvaSeleccionado(tipoIVA) // Actualiza en el ViewModel
                                    expandedIVA = false
                                    total = billViewModel.calculateTotal(baseImponible, irpf, ivaSeleccionadoState) // Recalcula el total DIRECTAMENTE
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = irpf,
                    onValueChange = { irpf = it },
                    label = { Text("IRPF") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Total: ${String.format("%.2f", total)} €", // Formato para 2 decimales y símbolo €
                    fontSize = 16.sp, modifier = Modifier.fillMaxWidth())
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Botón Registrar (sin cambios)
        item {
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
                        iva = ivaSeleccionadoState.porcentaje, // Guardamos el porcentaje del IVA seleccionado
                        irpf = irpf.toDoubleOrNull() ?: 0.0,
                        total = total,
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
}


// Componente para secciones plegables (sin cambios)
@Composable
fun ExpandableSection(
    title: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
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