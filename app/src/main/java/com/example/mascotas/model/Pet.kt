package com.example.mascotas.model

data class Pet(
    val name: String = "",
    val message: String = "",// La URL de la imagen
    val correo: String = "",
    val status: String = ""

) {
    // Constructor sin argumentos necesario para Firebase
    constructor() : this("", "", "","")
}