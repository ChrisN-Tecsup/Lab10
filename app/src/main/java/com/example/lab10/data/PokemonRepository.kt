package com.example.lab10.data

class PokemonRepository {
    private val pokemonList = mutableListOf<Pokemon>()

    fun obtenerPokemons(): List<Pokemon> = pokemonList

    fun agregarPokemon(pokemon: Pokemon) {
        pokemonList.add(pokemon)
    }

    fun actualizarPokemon(index: Int, pokemon: Pokemon) {
        if (index in pokemonList.indices) {
            pokemonList[index] = pokemon
        }
    }

    fun eliminarPokemon(index: Int) {
        if (index in pokemonList.indices) {
            pokemonList.removeAt(index)
        }
    }
}
