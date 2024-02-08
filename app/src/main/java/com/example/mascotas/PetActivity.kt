package com.example.mascotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.mascotas.ui.theme.MascotasTheme
import com.example.mascotas.viewmodel.PetsViewModel

class PetsActivity : ComponentActivity() {

    private val petsViewModel by viewModels<PetsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MascotasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PetsList(
                        pet = petsViewModel.petResponse,
                        error = petsViewModel.getErrorCon(),
                        onNameChanged = { newName ->
                            petsViewModel.updatePetName(newName) // Suponiendo que tengas una función para actualizar el nombre de la mascota en tu viewmodel
                        }
                    )
                    petsViewModel.getPet()
                }
            }
        }
    }
}
