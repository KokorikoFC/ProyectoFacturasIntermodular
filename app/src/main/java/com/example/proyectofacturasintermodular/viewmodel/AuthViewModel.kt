package com.example.proyectofacturasintermodular.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    var email = mutableStateOf("")
    var password = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var isLoggedIn = mutableStateOf(false)

    fun login(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        if (email.value.isNotEmpty() && password.value.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email.value, password.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isLoggedIn.value = true
                        onSuccess()
                    } else {
                        errorMessage.value = task.exception?.message ?: "Error desconocido"
                        onFailure(errorMessage.value)  
                    }
                }
        } else {
            errorMessage.value = "Por favor ingresa un correo y contraseña."
            onFailure(errorMessage.value)
        }
    }
}
