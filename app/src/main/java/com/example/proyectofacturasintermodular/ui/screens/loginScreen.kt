import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import com.example.proyectofacturasintermodular.R
import com.example.proyectofacturasintermodular.ui.theme.*
import com.example.proyectofacturasintermodular.viewmodel.AuthViewModel


@Composable
fun LoginScreen(navHostController: NavHostController, authViewModel: AuthViewModel) {
    val email = authViewModel.email.value
    val password = authViewModel.password.value
    val errorMessage = authViewModel.errorMessage.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray),
    ) {

        Image(
            painter = painterResource(id = R.drawable.background2),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Inicia sesión",
                    style = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Beige)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Debes iniciar sesión para poder registrar una factura",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Beige
                    ),
                    textAlign = TextAlign.Justify,
                    lineHeight = 30.sp
                )
            }
        }

        // Formulario de login
        Box(
            modifier = Modifier
                .fillMaxHeight(0.65f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 35.dp, topEnd = 30.dp))
                .background(Beige, shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp))
                .padding(bottom = 40.dp)
                .zIndex(1f) // Aseguramos que esté encima del círculo
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                // Campo de correo electrónico
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        "Correo electrónico",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gray, shape = RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    OutlinedTextField(
                        value = email,
                        onValueChange = { authViewModel.email.value = it },
                        placeholder = { Text("Correo Electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = Beige,
                            unfocusedTextColor = Beige,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                // Campo de contraseña
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .padding(bottom = 10.dp)
                ) {
                    Text(
                        "Contraseña",
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Medium,
                            color = Gray
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Gray, shape = RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    OutlinedTextField(
                        value = password,
                        onValueChange = { authViewModel.password.value = it },
                        placeholder = { Text("Contraseña") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color.Transparent,
                            focusedTextColor = Beige,
                            unfocusedTextColor = Beige,
                            focusedPlaceholderColor = Color.Gray,
                            unfocusedPlaceholderColor = Color.Gray,
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
                Column(
                    modifier = Modifier
                        .height(50.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (errorMessage.isNotEmpty()) {
                        Text(
                            text = errorMessage,
                            color = Color.Red,
                            style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Botón de login
                Button(
                    onClick = {
                        authViewModel.login(
                            onSuccess = {
                                // Navegar a la siguiente pantalla si el login es exitoso
                                navHostController.navigate("addBill")
                            },
                            onFailure = { message ->
                                // Manejo de error
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Red),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = Gray,
                        style = TextStyle(
                            fontSize = 22.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Gray
                        )
                    )
                }
            }
        }
    }
}
