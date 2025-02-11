package com.example.proyectofacturasintermodular.data.repository

import com.example.proyectofacturasintermodular.data.model.Bill
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class BillRepository {

    private val db = FirebaseFirestore.getInstance()

    suspend fun addBill(bill: Bill): Result<Unit> {
        return try {
            db.collection("bills") // Guarda directamente en la colección "bills"
                .document(bill.numeroFactura!!) // Usa numeroFactura como ID del documento
                .set(bill)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}