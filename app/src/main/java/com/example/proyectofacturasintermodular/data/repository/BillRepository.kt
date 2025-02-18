package com.example.proyectofacturasintermodular.data.repository

import com.example.proyectofacturasintermodular.data.model.Bill
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class BillRepository {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun addBill(bill: Bill): Result<Unit> {
        return try {
            val userId = auth.currentUser?.uid
            if (userId != null) {
                // Nueva ruta para guardar la factura: user -> userId -> bill
                db.collection("user").document(userId)
                    .collection("bill")
                    .document(bill.numeroFactura!!) // Usa numeroFactura como ID del documento
                    .set(bill)
                    .await()

                Result.success(Unit)
            } else {
                Result.failure(Exception("Usuario no autenticado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}