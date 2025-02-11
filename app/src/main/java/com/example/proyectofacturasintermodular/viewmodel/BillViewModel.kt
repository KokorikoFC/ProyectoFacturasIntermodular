package com.example.proyectofacturasintermodular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofacturasintermodular.data.model.Bill
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

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

    fun generarCodigoAlfanumerico(longitud: Int = 6): String {
        val caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..longitud)
            .map { caracteres.random() }
            .joinToString("")
    }

    suspend fun generarNumeroFactura(): String {
        var numeroFactura: String
        var existe: Boolean

        do {
            val nuevoCodigo = generarCodigoAlfanumerico()
            numeroFactura = "NFAC-$nuevoCodigo"

            // Consultamos en Firebase si ya existe
            val documentos = db.collection("bills")
                .whereEqualTo("numeroFactura", numeroFactura)
                .get()
                .await() // Esperamos la respuesta sin usar callback

            existe = !documentos.isEmpty
        } while (existe) // Si ya existe, generamos otro

        return numeroFactura
    }

    // Función para establecer el tipo de IVA seleccionado desde la UI
    fun actualizarIvaSeleccionado(tipoIVA: TipoIVA) {
        ivaSeleccionado = tipoIVA
    }
}