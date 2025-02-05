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

    fun addBill(bill : Bill) {
        viewModelScope.launch {
            val result = repository.addBill(bill)
        }
    }

        fun calculateTotal(baseImponible: String, iva: String, irpf: String): Double {
            val base = baseImponible.toDoubleOrNull() ?: 0.0
            val ivaValue = iva.toDoubleOrNull() ?: 0.0
            val irpfValue = irpf.toDoubleOrNull() ?: 0.0
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
            val documentos = db.collection("facturas")
                .whereEqualTo("numeroFactura", numeroFactura)
                .get()
                .await() // Esperamos la respuesta sin usar callback

            existe = !documentos.isEmpty
        } while (existe) // Si ya existe, generamos otro

        return numeroFactura
    }



}