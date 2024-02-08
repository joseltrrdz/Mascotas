package com.example.mascotas.network

import com.example.mascotas.model.Pet
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("random")
    suspend fun getPet(@Header("Cache-Control") cacheControl: String): Pet

    companion object {
        private var apiService: ApiService? = null

        fun getInstance(): ApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl("https://dog.ceo/api/breeds/image/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}