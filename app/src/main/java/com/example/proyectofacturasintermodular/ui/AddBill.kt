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
import androidx.core.app.NotificationCompat.Style
import com.example.proyectofacturasintermodular.ui.theme.Beige
import com.example.proyectofacturasintermodular.ui.theme.Gray
import com.example.proyectofacturasintermodular.ui.theme.Red


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
                    .padding(bottom = 100.dp, start = 24.dp, end = 24.dp, top = 24.dp)
                    .background(Gray, RoundedCornerShape(20.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 60.dp) // espacio para el botón inferior
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        "Añadir Factura",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Beige
                    )
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
                                containerColor = if (isIssued) Red else Beige
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Emitida", color = if (isIssued) Beige else Gray)
                        }

                        Button(
                            onClick = { isIssued = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isIssued) Red else Beige
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("Recibida", color = if (!isIssued) Beige else Gray)
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }

                item {

                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        if (isIssued) {
                            Text("Factura N°: $numeroFactura", fontSize = 18.sp, color = Beige)
                        } else {
                            StyledOutlinedTextField(
                                value = numeroFacturaManualState,
                                onValueChange = { numeroFacturaManualState = it },
                                placeholder = { Text("Factura N° (Recibida)") }
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
                            StyledOutlinedTextField(
                                value = nombreEmisor,
                                onValueChange = { nombreEmisor = it },
                                placeholder = { Text("Empresa:") }
                            )

                            StyledOutlinedTextField(
                                value = nifEmisor,
                                onValueChange = { nifEmisor = it },
                                placeholder = { Text("NIF:") }
                            )

                            StyledOutlinedTextField(
                                value = direccionEmisor,
                                onValueChange = { direccionEmisor = it },
                                placeholder = { Text("Dirección:") }
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
                            StyledOutlinedTextField(
                                value = nombreReceptor,
                                onValueChange = { nombreReceptor = it },
                                placeholder = { Text("Cliente:") }
                            )
                            StyledOutlinedTextField(
                                value = nifReceptor,
                                onValueChange = { nifReceptor = it },
                                placeholder = { Text("NIF/CIF:") }
                            )
                            StyledOutlinedTextField(
                                value = direccionReceptor,
                                onValueChange = { direccionReceptor = it },
                                placeholder = { Text("Dirección:") }
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
                            StyledOutlinedTextField(
                                value = baseImponible,
                                onValueChange = { baseImponible = it },
                                placeholder = { Text("Base imponible:") }
                            )
                            ExposedDropdownMenuBox(
                                expanded = expandedIVA,
                                onExpandedChange = { expandedIVA = !expandedIVA }
                            ) {
                                OutlinedTextFieldStyle(
                                    value = ivaSeleccionadoState.nombre,
                                    onValueChange = { },
                                    label = { Text("IVA:", color = Beige) },
                                    readOnly = true,
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(
                                            expanded = expandedIVA
                                        )
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor(),
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedIVA,
                                    onDismissRequest = { expandedIVA = false }
                                ) {
                                    billViewModel.tiposIVA.forEach { tipoIVA ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = "${tipoIVA.nombre} (${tipoIVA.porcentaje}%)",
                                                    color = Gray
                                                )
                                            },
                                            onClick = {
                                                ivaSeleccionadoState = tipoIVA
                                                billViewModel.actualizarIvaSeleccionado(tipoIVA)
                                                expandedIVA = false
                                                total = billViewModel.calculateTotal(
                                                    baseImponible,
                                                    irpf,
                                                    ivaSeleccionadoState
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                            StyledOutlinedTextField(
                                value = irpf,
                                onValueChange = { irpf = it },
                                placeholder = { Text("IRPF:") }
                            )
                            Text(
                                "Total: ${String.format("%.2f", total)} €",
                                fontSize = 20.sp,
                                modifier = Modifier.fillMaxWidth(),
                                color = Beige,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(25.dp))
                }


            }
            Button(
                onClick = {
                billViewModel.addBill(
                    Bill(
                        numeroFactura = if (isIssued) numeroFactura else numeroFacturaManualState,
                        fechaEmision = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        empresaEmisor = nombreEmisor,
                        nifEmisor = nifEmisor,
                        direccionEmisor = direccionEmisor,
                        clienteReceptor = nombreReceptor,
                        nifReceptor = nifReceptor,
                        direccionReceptor = direccionReceptor,
                        baseImponible = baseImponible.toDoubleOrNull(),
                        iva = ivaSeleccionadoState.porcentaje,
                        irpf = irpf.toDoubleOrNull(),
                        total = total,
                        esFacturaEmitida = isIssued
                    )
                )
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(20.dp).fillMaxWidth(),
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
                .padding(vertical = 12.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Contraer" else "Expandir",
                tint = Beige
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Beige)
        }
        if (isExpanded) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 8.dp,
                    bottom = 8.dp,
                    end = 16.dp
                )
            ) {
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
        trailingIcon = trailingIcon,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Beige,
            unfocusedTextColor = Beige
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StyledOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Beige, shape = RoundedCornerShape(8.dp))
            .padding(2.dp)
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = placeholder,
            singleLine = singleLine,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = keyboardOptions,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = Color.Transparent,
                focusedBorderColor = Color.Transparent,
                focusedTextColor = Gray,
                unfocusedTextColor = Gray,
                focusedPlaceholderColor = Color.Gray,
                unfocusedPlaceholderColor = Color.Gray,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )

    }
    Spacer(modifier = Modifier.height(20.dp))
}