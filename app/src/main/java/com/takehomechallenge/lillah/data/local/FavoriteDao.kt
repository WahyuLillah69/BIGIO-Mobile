package com.takehomechallenge.lillah.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite_character")
    fun getFavorites(): Flow<List<FavoriteCharacterEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(character: FavoriteCharacterEntity)

    @Delete
    suspend fun deleteFavorite(character: FavoriteCharacterEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM Favorite_character WHERE id = :id)")
    suspend fun isFavorite(id: Int): Boolean
}