package com.takehomechallenge.lillah.data.remote

import com.takehomechallenge.lillah.data.model.Character
import com.takehomechallenge.lillah.data.model.CharacterResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickMortyApi {

    @GET("character")
    suspend fun getCharacters(): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterDetail(
        @Path("id") id: Int
    ): Character

    @GET("character")
    suspend fun searchCharacter(
        @Query("name") name: String
    ): CharacterResponse
}