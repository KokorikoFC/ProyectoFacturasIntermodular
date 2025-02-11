package com.example.proyectofacturasintermodular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofacturasintermodular.data.model.Bill
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.Locale

class BillViewModel : ViewModel() {

    private val repository = BillRepository()
    val db = FirebaseFirestore.getInstance()

    // Definimos los tipos de IVA como una lista de Data Classes
    data class TipoIVA(val nombre: String, val porcentaje: Double)

    val tiposIVA = listOf(
        TipoIVA("General", 21.0),
        TipoIVA("Reducido", 10.0),
        TipoIVA("Superreducido", 4.0),
        TipoIVA("Exento", 0.0)
    )

    // Variable para el tipo de IVA seleccionado. Inicialmente "General"
    var ivaSeleccionado: TipoIVA = tiposIVA[0] // Inicializado a IVA General


    fun addBill(bill : Bill) {
        viewModelScope.launch {
            val result = repository.addBill(bill)
        }
    }

    // Función modificada para usar el tipo de IVA seleccionado (TipoIVA como parámetro)
    fun calculateTotal(baseImponible: String, irpf: String, tipoIVA: TipoIVA): Double {
        val base = baseImponible.toDoubleOrNull() ?: 0.0
        val irpfValue = irpf.toDoubleOrNull() ?: 0.0
        val ivaValue = tipoIVA.porcentaje // Obtiene el porcentaje del IVA pasado como argumento
        return base + (base * ivaValue / 100) - (base * irpfValue / 100)
    }

    // Función para generar un código alfanumérico aleatorio (ya no se usa para el número de factura)
    fun generarCodigoAlfanumerico(longitud: Int = 6): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..longitud)
            .map { caracteres.random() }
            .joinToString("")
    }

    suspend fun generarNumeroFactura(): String {
        var numeroFactura: String
        var existe: Boolean
        val prefijoFactura = "FAC-"

        do {
            val lastBill = getLastBill()
            println("generarNumeroFactura: getLastBill devolvió: ${lastBill?.numeroFactura}") // Log

            val nextNumber = if (lastBill != null) {
                val lastId = lastBill.numeroFactura?.removePrefix(prefijoFactura) ?: "0"
                val number = lastId.toIntOrNull() ?: 0
                number + 1
            } else {
                1
            }
            val formattedNumber = String.format(Locale.getDefault(), "%05d", nextNumber)
            numeroFactura = prefijoFactura + formattedNumber
            println("generarNumeroFactura: Número de factura generado: $numeroFactura") // Log


            // Consultamos en Firebase si ya existe (aunque con el nuevo método no debería existir duplicados)
            val documentos = db.collection("bills")
                .whereEqualTo("numeroFactura", numeroFactura)
                .get()
                .await()

            existe = !documentos.isEmpty
        } while (existe)

        return numeroFactura
    }


    private suspend fun getLastBill(): Bill? {
        return try {
            val querySnapshot = db.collection("bills")
                .orderBy("numeroFactura", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val bill = querySnapshot.documents.firstOrNull()?.toObject(Bill::class.java)
                println("getLastBill: Última factura encontrada: ${bill?.numeroFactura}") // Log
                bill
            } else {
                println("getLastBill: No se encontraron facturas.") // Log
                null
            }
        } catch (e: Exception) {
            println("getLastBill: Error al obtener la última factura: ${e.message}") // Log de error
            null
        }
    }


    // Función para establecer el tipo de IVA seleccionado desde la UI
    fun actualizarIvaSeleccionado(tipoIVA: TipoIVA) {
        ivaSeleccionado = tipoIVA
    }
}