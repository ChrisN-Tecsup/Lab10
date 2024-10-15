package com.example.lab10

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.lab10.data.PokeApiService
import com.example.lab10.data.Pokemon
import com.example.lab10.data.PokemonRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private lateinit var pokeApiService: PokeApiService
    private val repository = PokemonRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApiService = retrofit.create(PokeApiService::class.java)

        setContent {
            PokeApp()
        }
    }

    @Composable
    fun PokeApp() {
        var pokemonId by remember { mutableStateOf("") }
        var pokemonList by remember { mutableStateOf<List<Pokemon>>(emptyList()) }
        var errorMessage by remember { mutableStateOf("") }
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Campo de texto para ingresar el ID del Pokémon
            TextField(
                value = pokemonId,
                onValueChange = { pokemonId = it },
                label = { Text("ID del Pokémon") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorMessage.isNotBlank()
            )

            if (errorMessage.isNotBlank()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (pokemonId.isNotBlank()) {
                    coroutineScope.launch {
                        try {
                            val id = pokemonId.toInt()
                            val pokemon = pokeApiService.obtenerPokemon(id)
                            repository.agregarPokemon(pokemon)
                            pokemonList = repository.obtenerPokemons()
                            pokemonId = ""
                            errorMessage = ""
                        } catch (e: Exception) {
                            errorMessage = "Error: ${e.message}"
                            Log.e("PokeAPI", "Error: ${e.message}")
                        }
                    }
                } else {
                    errorMessage = "Por favor, ingrese un ID válido."
                }
            }) {
                Text("Agregar Pokémon")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Lista de Pokémon:")
            pokemonList.forEach { p ->
                Text(text = "${p.id}: ${p.name}", color = Color.Black)
            }
        }
    }
}
