package com.takehomechallenge.lillah.data.repository

import com.takehomechallenge.lillah.data.local.FavoriteCharacterEntity
import com.takehomechallenge.lillah.data.local.FavoriteDao
import com.takehomechallenge.lillah.data.remote.RickMortyApi

class CharacterRepository(
    private val api: RickMortyApi,
    private val dao: FavoriteDao
) {
    // API
    suspend fun getCharacters() = api.getCharacters().results
    suspend fun getCharacterDetail(id: Int) = api.getCharacterDetail(id)
    suspend fun searchCharacter(name: String) = api.searchCharacter(name).results

    // Favorite
    fun getFavorites() = dao.getFavorites()

    suspend fun addFavorite(character: FavoriteCharacterEntity) {
        dao.insertFavorite(character)
    }

    suspend fun removeFavorite(character: FavoriteCharacterEntity) {
        dao.deleteFavorite(character)
    }

    suspend fun isFavorite(id: Int): Boolean {
        return dao.isFavorite(id)
    }
}
