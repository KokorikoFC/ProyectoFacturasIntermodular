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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.proyectofacturasintermodular.R // Asegúrate de que R se importa correctamente
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.KeyboardArrowDown


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBill(navHostController: NavHostController, billViewModel: BillViewModel) {

    var isIssued by remember { mutableStateOf(true) }
    var numeroFactura by remember { mutableStateOf("") }
    var numeroFacturaManualState by remember { mutableStateOf("") }
    var nombreEmisor by remember { mutableStateOf("") }
    var nifEmisor by remember { mutableStateOf("") }
    var direccionEmisor by remember { mutableStateOf("") }
    var nombreReceptor by remember { mutableStateOf("") }
    var nifReceptor by remember { mutableStateOf("") }
    var direccionReceptor by remember { mutableStateOf("") }
    var baseImponible by remember { mutableStateOf("") }
    var irpf by remember { mutableStateOf("") }

    var ivaSeleccionadoState by remember { mutableStateOf(billViewModel.tiposIVA[0]) }
    var total by remember(baseImponible, irpf, ivaSeleccionadoState) {
        mutableStateOf(billViewModel.calculateTotal(baseImponible, irpf, ivaSeleccionadoState))
    }

    var isIssuerExpanded by remember { mutableStateOf(false) }
    var isReceiverExpanded by remember { mutableStateOf(false) }
    var isAmountsExpanded by remember { mutableStateOf(false) }
    var expandedIVA by remember { mutableStateOf(false) }

    // State para controlar el Snackbar
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Función para resetear los campos
    fun resetFields() {
        numeroFacturaManualState = ""
        nombreEmisor = ""
        nifEmisor = ""
        direccionEmisor = ""
        nombreReceptor = ""
        nifReceptor = ""
        direccionReceptor = ""
        baseImponible = ""
        irpf = ""
        total = 0.0
    }


    LaunchedEffect(isIssued) {
        if (isIssued) {
            numeroFactura = billViewModel.generarNumeroFactura()
        } else {
            numeroFactura = ""
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(bottom = 50.dp, start = 24.dp, end = 24.dp, top = 24.dp)
                    .background(Color.White.copy(alpha = 0.9f), RoundedCornerShape(20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 60.dp) // espacio para el botón inferior
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text("Añadir Factura", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { isIssued = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isIssued) Color.Blue else Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Emitida", color = Color.White)
                        }

                        Button(
                            onClick = { isIssued = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isIssued) Color.Blue else Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Recibida", color = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        if (isIssued) {
                            Text("Factura N°: $numeroFactura", fontSize = 18.sp, color = Color.Black)
                        } else {
                            OutlinedTextField(
                                value = numeroFacturaManualState,
                                onValueChange = { numeroFacturaManualState = it },
                                label = { Text("Factura N° (Recibida)", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp)
                            )

                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    ExpandableSectionStyle(
                        title = "Datos del emisor",
                        isExpanded = isIssuerExpanded,
                        onToggle = { isIssuerExpanded = !isIssuerExpanded }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            OutlinedTextFieldStyle(
                                value = nombreEmisor,
                                onValueChange = { nombreEmisor = it },
                                label = { Text("Empresa:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextFieldStyle(
                                value = nifEmisor,
                                onValueChange = { nifEmisor = it },
                                label = { Text("NIF:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextFieldStyle(
                                value = direccionEmisor,
                                onValueChange = { direccionEmisor = it },
                                label = { Text("Dirección:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    ExpandableSectionStyle(
                        title = "Datos del receptor",
                        isExpanded = isReceiverExpanded,
                        onToggle = { isReceiverExpanded = !isReceiverExpanded }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            OutlinedTextFieldStyle(
                                value = nombreReceptor,
                                onValueChange = { nombreReceptor = it },
                                label = { Text("Cliente:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextFieldStyle(
                                value = nifReceptor,
                                onValueChange = { nifReceptor = it },
                                label = { Text("NIF/CIF:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextFieldStyle(
                                value = direccionReceptor,
                                onValueChange = { direccionReceptor = it },
                                label = { Text("Dirección:", color = Color.Black) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    ExpandableSectionStyle(
                        title = "Importes",
                        isExpanded = isAmountsExpanded,
                        onToggle = { isAmountsExpanded = !isAmountsExpanded }
                    ) {
                        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                            OutlinedTextFieldStyle(
                                value = baseImponible,
                                onValueChange = { baseImponible = it },
                                label = { Text("Base imponible:", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandedIVA,
                                onExpandedChange = { expandedIVA = !expandedIVA }
                            ) {
                                OutlinedTextFieldStyle( // Apply style here as well for consistency
                                    value = ivaSeleccionadoState.nombre,
                                    onValueChange = { },
                                    label = { Text("IVA:", color = Color.Black) },
                                    readOnly = true, // Add readOnly parameter
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedIVA) }, // Add trailingIcon parameter
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
                                            text = { Text(text = "${tipoIVA.nombre} (${tipoIVA.porcentaje}%)", color = Color.Black) },
                                            onClick = {
                                                ivaSeleccionadoState = tipoIVA
                                                billViewModel.actualizarIvaSeleccionado(tipoIVA)
                                                expandedIVA = false
                                                total = billViewModel.calculateTotal(baseImponible, irpf, ivaSeleccionadoState)
                                            }
                                        )
                                    }
                                }
                            }

                            OutlinedTextFieldStyle(
                                value = irpf,
                                onValueChange = { irpf = it },
                                label = { Text("IRPF:", color = Color.Black) },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Text("Total: ${String.format("%.2f", total)} €",
                                fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                }


            }
            Button(
                onClick = {
                    //TODO: Guardar factura y mostrar mensaje de éxito o error
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("Añadir Factura", color = Color.White)
            }
        }
    }
}


@Composable
fun ExpandableSectionStyle(
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
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Contraer" else "Expandir",
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        if (isExpanded) {
            Column(modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp, end = 16.dp)) {
                content()
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedTextFieldStyle(
    value: String,
    onValueChange: (String) -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        modifier = modifier.padding(bottom = 8.dp),
        keyboardOptions = keyboardOptions,
        shape = RoundedCornerShape(8.dp),
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}