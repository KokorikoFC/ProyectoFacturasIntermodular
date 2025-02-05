package com.example.proyectofacturasintermodular.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.proyectofacturasintermodular.data.model.Bill
import com.example.proyectofacturasintermodular.data.repository.BillRepository
import kotlinx.coroutines.launch

class BillViewModel : ViewModel() {

    private val repository = BillRepository()

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




}