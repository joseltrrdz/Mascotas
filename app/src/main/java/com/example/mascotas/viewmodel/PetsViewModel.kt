package com.example.mascotas.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mascotas.model.Pet
import com.example.mascotas.network.ApiService
import kotlinx.coroutines.launch


class PetsViewModel : ViewModel() {

    var petResponse: Pet? by mutableStateOf(null)
    var errorMessage: String by mutableStateOf("")
    var error: Boolean by mutableStateOf(false)

    fun getPet(forceReload: Boolean = false) {
        viewModelScope.launch {
            Log.d("PetsViewModel", "Solicitando nueva mascota...")

            if (forceReload || petResponse == null) {
                val apiService = ApiService.getInstance()
                try {
                    val pet = apiService.getPet("no-cache")
                    petResponse = pet
                    error = false

                    Log.d("PetsViewModel", "Se obtuvo una nueva mascota: ${petResponse?.message}")
                } catch (e: Exception) {
                    errorMessage = e.message.toString()
                    error = true
                    Log.e("PetsViewModel", "Error al obtener mascota: $errorMessage")
                }
            }
        }
    }



    fun getErrorCon(): Boolean = error

    fun updatePetName(newName: String) {
        petResponse?.let { currentPet ->
            petResponse = currentPet.copy(name = newName)
        }
    }
}