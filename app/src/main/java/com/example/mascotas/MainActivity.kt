package com.example.mascotas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mascotas.model.Pet
import com.example.mascotas.ui.theme.MascotasTheme
import com.example.mascotas.view.PetItem
import com.example.mascotas.view.PetItem2
import com.example.mascotas.viewmodel.AuthViewModel
import com.example.mascotas.viewmodel.PetsViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    companion object {
        var currentUserEmail: String? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MascotasTheme {
                val navController = rememberNavController()
                val authViewModel: AuthViewModel = viewModel()

                NavHost(navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(navController, authViewModel)
                    }
                    composable("main") {
                        val petsViewModel: PetsViewModel = viewModel()
                        val authViewModel: AuthViewModel = viewModel()

                        MainActivityScreen(petsViewModel, authViewModel, navController)
                    }
                    composable("my_pets") {
                        MyPetsScreen(authViewModel)
                    }
                    composable("all_pets") {
                        AllPetsScreen(authViewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun AllPetsScreen(authViewModel: AuthViewModel) {
    val scope = rememberCoroutineScope()
    var allPets by remember { mutableStateOf<List<Pet>>(emptyList()) }

    DisposableEffect(Unit) {
        scope.launch {
            authViewModel.getAllPets { pets ->
                allPets = pets
            }
        }
        onDispose { }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(allPets) { pet ->
            PetItem2(pet = pet)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MyPetsScreen(authViewModel: AuthViewModel) {
    val scope = rememberCoroutineScope()
    var myPets by remember { mutableStateOf<List<Pet>>(emptyList()) }

    DisposableEffect(Unit) {
        scope.launch {
            authViewModel.getMyPets { pets ->
                myPets = pets
            }
        }
        onDispose { }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(myPets) { pet ->
            PetItem2(pet = pet)
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
@Composable
fun LoginScreen(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isRegistering by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Image(
            painter = painterResource(id = R.drawable.pxfuel),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Mascoadopta",
                style = TextStyle(
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Cursive,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(200.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (isRegistering) {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirma la contraseña") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = {
                        if (isRegistering) {
                            if (password == confirmPassword) {
                                authViewModel.createUserWithEmailAndPassword(email, password) {
                                    MainActivity.currentUserEmail = email
                                    navController.navigate("main")
                                }
                            } else {
                                // Show error message for password mismatch
                            }
                        } else {
                            authViewModel.signInWithEmailAndPassword(email, password) {
                                MainActivity.currentUserEmail = email
                                navController.navigate("main")
                            }
                        }
                    }
                ) {
                    Text(if (isRegistering) "Registrarse" else "Iniciar Sesión")
                }
                Button(
                    onClick = { isRegistering = !isRegistering }
                ) {
                    Text(if (isRegistering) "¿Ya tienes cuenta? Inicia sesión" else "Crea una cuenta")
                }
            }
        }
    }
}

@Composable
fun MainActivityScreen(
    petsViewModel: PetsViewModel,
    authViewModel: AuthViewModel,
    navController: NavController,
) {
    DisposableEffect(petsViewModel) {
        petsViewModel.getPet(forceReload = true)
        onDispose { }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        PetsList(
            pet = petsViewModel.petResponse,
            error = petsViewModel.getErrorCon(),
            onNameChanged = { newName ->
                petsViewModel.updatePetName(newName)
            }
        )

        Spacer(modifier = Modifier.height(6.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Button(
                onClick = {
                    petsViewModel.getPet(forceReload = true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text("Siguiente mascota")
            }

            Button(
                onClick = {
                    val pet = petsViewModel.petResponse
                    if (pet != null) {
                        val currentUserEmail = MainActivity.currentUserEmail
                        if (currentUserEmail != null) {
                            authViewModel.savePetForCurrentUser(pet.name ?: "", pet.message ?: "", currentUserEmail,
                                successCallback = {
                                    petsViewModel.getPet(forceReload = true)
                                },
                                errorCallback = { errorMessage ->
                                    // Handle error while saving pet
                                }
                            )
                        } else {
                            Log.e("MainActivity", "Current user email is null")
                        }
                    } else {
                        Log.e("MainActivity", "Pet object is null")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp)
            ) {
                Text("Adoptar mascota")
            }
        }

        Button(
            onClick = {
                navController.navigate("my_pets")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text("Ver mis mascotas")
        }
        Button(
            onClick = {
                navController.navigate("all_pets")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp)
        ) {
            Text("Ver todas las mascotas")
        }
    }
}

@Composable
fun PetsList(pet: Pet?, error: Boolean, onNameChanged: (String) -> Unit) {
    if (error) {
        Box(Modifier.fillMaxSize()) {
            Text(
                text = "Connection error",
                fontSize = 30.sp
            )
        }
    } else {
        pet?.let {
            Log.d("PetsList", "Nombre de la mascota: ${it.name}") // Comprobación del nombre de la mascota
            Log.d("PetsList", "URL de la imagen: ${it.message}") // Comprobación de la URL de la imagen
            PetItem(pet = it, onNameChanged = onNameChanged)
        }
    }
}
