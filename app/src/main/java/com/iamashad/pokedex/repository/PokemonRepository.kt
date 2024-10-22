package com.iamashad.pokedex.repository

import com.iamashad.pokedex.model.Pokemon
import com.iamashad.pokedex.model.PokemonList
import com.iamashad.pokedex.network.PokeAPI
import com.iamashad.pokedex.utils.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: PokeAPI
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getPokemonList(limit,offset)
        } catch (e: Exception) {
            return Resource.Error(null, "An error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }
    suspend fun getPokemonInfo(name: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(name)
        } catch (e: Exception) {
            return Resource.Error(null, "An error occurred: ${e.message}")
        }
        return Resource.Success(response)
    }
}