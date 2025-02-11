package com.example.proyectofacturasintermodular.data.model

data class Bill(

    // Datos generales
    val numeroFactura: String? = null, // Valor por defecto null
    val fechaEmision: String? = null, // Valor por defecto null

    // Datos del emisor
    val empresaEmisor: String? = null, // Valor por defecto null
    val nifEmisor: String? = null, // Valor por defecto null
    val direccionEmisor: String? = null, // Valor por defecto null

    // Datos del receptor
    val clienteReceptor: String? = null, // Valor por defecto null
    val nifReceptor: String? = null, // Valor por defecto null
    val direccionReceptor: String? = null, // Valor por defecto null

    // Datos de la factura
    val baseImponible: Double? = 0.0, // Valor por defecto 0.0
    val iva: Double? = 0.0, // Valor por defecto 0.0
    val irpf: Double? = 0.0, // Valor por defecto 0.0
    val total: Double? = 0.0, // Valor por defecto 0.0

    // Datos adicionales
    val esFacturaEmitida: Boolean? = false // Valor por defecto false
)