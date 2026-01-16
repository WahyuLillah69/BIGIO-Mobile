package com.takehomechallenge.lillah.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_character")
data class FavoriteCharacterEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val image: String
)