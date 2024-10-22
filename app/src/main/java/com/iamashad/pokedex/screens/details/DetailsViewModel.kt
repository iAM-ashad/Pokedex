package com.iamashad.pokedex.screens.details

import androidx.lifecycle.ViewModel
import com.iamashad.pokedex.model.Pokemon
import com.iamashad.pokedex.repository.PokemonRepository
import com.iamashad.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor (
    private val repository: PokemonRepository
): ViewModel() {
    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }
}