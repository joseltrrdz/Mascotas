package com.example.mascotas.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.example.mascotas.model.Pet

@Composable
fun PetItem(pet: Pet, onNameChanged: (String) -> Unit) {
    // Variable de estado para el valor del campo de texto
    var name by remember { mutableStateOf("") }

    Log.d("PetItem", "Nombre de la mascota: ${pet.name}") // Comprobación del nombre de la mascota
    Log.d("PetItem", "URL de la imagen: ${pet.message}") // Comprobación de la URL de la imagen

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(620.dp)
            .background(
                color = Color(0xFFA2D8CA),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center)
        ) {
            // Contenedor de la imagen
            Row (
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pet.message)
                            .transformations(CircleCropTransformation())
                            .build(),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(300.dp)
                            .align(Alignment.Center)
                            .aspectRatio(1f),
                        contentDescription = pet.status
                    )
                }
            }

            // Contenedor del campo de texto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                // Texto "Nombre:"
                Text(
                    text = "Nombre:",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontFamily = FontFamily.Cursive),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )

                // OutlinedTextField para editar el nombre
                OutlinedTextField(
                    value = name, // Usar la variable de estado para el valor del campo de texto
                    onValueChange = {
                        name = it // Actualizar el valor de la variable de estado al cambiar el texto
                        onNameChanged(it) // Llamar a la función onNameChanged con el nuevo valor
                    },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun PetItem2(pet: Pet) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(530.dp)
            .background(
                color = Color(0xFFA2D8CA),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Contenedor de la imagen
            Row (
                modifier = Modifier.fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(
                            color = Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pet.message)
                            .transformations(CircleCropTransformation())
                            .build(),
                        modifier = Modifier
                            .padding(10.dp)
                            .size(300.dp)
                            .align(Alignment.Center)
                            .aspectRatio(1f),
                        contentDescription = pet.status
                    )
                }
            }
            // Contenedor del nombre de la mascota
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 16.dp)
            ) {
                // Texto "Nombre:"
                Text(
                    text = "Nombre: ${pet.name}",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontFamily = FontFamily.Cursive),
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp
                )
                Text(
                    text = "Dueño: ${pet.correo}",
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontFamily = FontFamily.Cursive),
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp
                )
            }
        }
    }
}