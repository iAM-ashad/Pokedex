package com.iamashad.pokedex.screens.home

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import com.iamashad.pokedex.model.PokedexListEntry
import com.iamashad.pokedex.repository.PokemonRepository
import com.iamashad.pokedex.utils.Constants
import com.iamashad.pokedex.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    private var currPage = 0

    var pokemonList = mutableStateOf<List<PokedexListEntry>>(listOf())
    var loadingError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonlist = listOf<PokedexListEntry>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonPaginated()
    }
    fun searchPokemon(query: String) {
        val listToSearch = if (isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonlist
        }
        viewModelScope.launch(Dispatchers.Default) {
            if (query.isEmpty()) {
                pokemonList.value = cachedPokemonlist
                isSearching.value = false
                isSearchStarting = true
                //return@launch
            } else {
                val results = listToSearch.filter {
                    it.pokemonName.contains(query.trim(), ignoreCase = true) || it.number.toString() == query.trim()
                }
                if (isSearchStarting) {
                    cachedPokemonlist = pokemonList.value
                    isSearchStarting = false
                }
                pokemonList.value = results
                isSearching.value = true
            }
        }
    }
    fun loadPokemonPaginated() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(Constants.PAGE_SIZE, currPage* Constants.PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = currPage * Constants.PAGE_SIZE >= result.data!!.count
                    val pokedexEntries = result.data.results.mapIndexed { index, entry->
                        val number = if (entry.url.endsWith("/")) {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            entry.url.dropLast(1).takeLastWhile { it.isDigit() }
                        }
                        val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
                        PokedexListEntry(entry.name.capitalize(locale = Locale.ROOT), url, number.toInt())
                    }
                    currPage++
                    loadingError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokedexEntries
                }
                else -> {
                    loadingError.value = "Error Occurred"
                    isLoading.value = false
                }
            }
        }
    }


    fun calcDominantColor(drawable: Drawable, onFinish: (Color)-> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)
        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}