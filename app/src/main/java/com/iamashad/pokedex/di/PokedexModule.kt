package com.iamashad.pokedex.di

import com.iamashad.pokedex.network.PokeAPI
import com.iamashad.pokedex.repository.PokemonRepository
import com.iamashad.pokedex.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokedexModule {

    @Singleton
    @Provides
    fun providePokemonRepository(api: PokeAPI) = PokemonRepository(api)

    @Singleton
    @Provides
    fun providePokemonApi(): PokeAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PokeAPI::class.java)
    }
}