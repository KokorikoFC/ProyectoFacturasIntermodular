package com.example.proyectofacturasintermodular.data.model

data class Bill(

    // Datos generales
    val numeroFactura: String,
    val fechaEmision: String,

    // Datos del emisor
    val empresaEmisor: String,
    val nifEmisor: String,
    val direccionEmisor: String,

    // Datos del receptor
    val clienteReceptor: String,
    val nifReceptor: String,
    val direccionReceptor: String,

    // Datos de la factura
    val baseImponible: Double,
    val iva: Double,
    val irpf: Double,
    val total: Double,

    // Datos adicionales
    val esFacturaEmitida: Boolean
)

