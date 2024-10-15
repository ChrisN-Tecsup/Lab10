package com.example.lab10.data

import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApiService {
    @GET("pokemon/{id}")
    suspend fun obtenerPokemon(@Path("id") id: Int): Pokemon
}
