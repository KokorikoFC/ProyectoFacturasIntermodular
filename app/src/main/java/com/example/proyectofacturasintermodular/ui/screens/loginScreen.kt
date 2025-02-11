import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.proyectofacturasintermodular.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth



@Composable
fun LoginScreen(navHostController: NavHostController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val email = authViewModel.email.value
    val password = authViewModel.password.value
    val errorMessage = authViewModel.errorMessage.value

    // Función para manejar el login
    fun login() {
        authViewModel.login(
            onSuccess = {
                // Si el login es exitoso, navegamos a otra pantalla
                navHostController.navigate("addBill")
            },
            onFailure = { message ->
                // Si el login falla, mostramos el mensaje de error
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de correo electrónico
        TextField(
            value = email,
            onValueChange = { authViewModel.email.value = it },
            label = { Text("Correo Electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Campo de contraseña
        TextField(
            value = password,
            onValueChange = { authViewModel.password.value = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de login
        Button(onClick = { login() }) {
            Text("Iniciar sesión")
        }

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}