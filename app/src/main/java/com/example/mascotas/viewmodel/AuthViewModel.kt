package com.example.mascotas.viewmodel
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mascotas.model.Pet
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = Firebase.auth
    private val _loading = MutableLiveData(false)

    fun getMyPets(callback: (List<Pet>) -> Unit) {
        val currentUser = getCurrentUser()
        currentUser?.let { user ->
            val uid = user.uid
            val petsReference = FirebaseDatabase.getInstance().reference.child("users").child(uid).child("pets")
            petsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val petsList = mutableListOf<Pet>()
                    for (petSnapshot in snapshot.children) {
                        val pet = petSnapshot.getValue(Pet::class.java)
                        pet?.let {
                            petsList.add(it)
                        }
                    }
                    callback(petsList)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Manejar errores de lectura de Firebase
                }
            })
        }
    }

    fun getAllPets(callback: (List<Pet>) -> Unit) {
        val petsReference = FirebaseDatabase.getInstance().reference.child("users")
        petsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val allPetsList = mutableListOf<Pet>()
                for (userSnapshot in snapshot.children) {
                    val userPetsSnapshot = userSnapshot.child("pets")
                    for (petSnapshot in userPetsSnapshot.children) {
                        val pet = petSnapshot.getValue(Pet::class.java)
                        pet?.let {
                            allPetsList.add(it)
                        }
                    }
                }
                callback(allPetsList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle Firebase read errors
            }
        })
    }

    fun savePetForCurrentUser(name: String, message: String, correo: String, successCallback: () -> Unit, errorCallback: (String) -> Unit) {
        val currentUser = getCurrentUser()
        if (currentUser != null) {
            val userId = currentUser.uid
            val petsRef = FirebaseDatabase.getInstance().reference.child("users").child(userId).child("pets")
            val petId = petsRef.push().key ?: return
            val petData = mapOf(
                "name" to name,
                "message" to message,
                "correo" to correo
                // Puedes agregar más datos de la mascota aquí si es necesario
            )
            petsRef.child(petId).setValue(petData)
                .addOnSuccessListener {
                    successCallback()
                }
                .addOnFailureListener { error ->
                    errorCallback(error.message ?: "Error al guardar la mascota")
                }
        } else {
            errorCallback("Usuario no autenticado")
        }
    }
    fun isValidEmail(email: String): Boolean {
        val pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) =
        viewModelScope.launch {
            try {
                if (!isValidEmail(email)) {
                    Log.e("MascotaFeliz", "La dirección de correo electrónico está mal formateada")
                    // Manejar el error aquí, como mostrar un mensaje al usuario
                    return@launch
                }

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d("MascotaFeliz", "signInWithEmailAndPassword logueado")
                            home()
                        } else {
                            Log.d("MascotaFeliz", "signInWithEmailAndPassword: ${task.result.toString()}")
                            // Manejar el caso de no éxito, por ejemplo, mostrar un mensaje de error al usuario
                        }
                    }
                    .addOnFailureListener { exception ->
                        // Manejar excepciones de Firebase Auth aquí
                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> {
                                Log.e("MascotaFeliz", "Credenciales de inicio de sesión incorrectas")
                                // Manejar el error de credenciales inválidas aquí
                            }
                            is FirebaseAuthInvalidUserException -> {
                                Log.e("MascotaFeliz", "El usuario no está registrado")
                                // Manejar el error de usuario no registrado aquí
                            }
                            else -> {
                                Log.e("MascotaFeliz", "Ha ocurrido un error durante la autenticación: ${exception.message}")
                                // Manejar otros errores de autenticación aquí
                            }
                        }
                        // Aquí puedes realizar acciones adicionales, como mostrar mensajes al usuario
                    }
            } catch (ex: Exception) {
                // Capturar cualquier excepción no esperada
                Log.e("MascotaFeliz", "Excepción inesperada durante el inicio de sesión: ${ex.message}")
                // Aquí puedes manejar la excepción, por ejemplo, mostrar un mensaje de error al usuario
            }
        }

    fun createUserWithEmailAndPassword(
        email:String,
        password: String,
        home: () -> Unit
    ){
        if(_loading.value == false){
            _loading.value = true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        home()
                    }
                    else{
                        Log.d("MascotaFeliz","createUserWithEmailAndPassword: ${task.result}")
                    }
                    _loading.value = false
                }
        }
    }
    // Observador de cambios en la autenticación
    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    // Función para iniciar sesión con correo electrónico y contraseña


    // Función para cerrar sesión
    suspend fun signOut() {
        auth.signOut()
    }

    // Función para verificar si el usuario está autenticado
    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}