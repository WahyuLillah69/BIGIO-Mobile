package com.takehomechallenge.lillah.ui.favorite

import com.takehomechallenge.lillah.data.local.FavoriteCharacterEntity

sealed class FavoriteState {
    object Loading : FavoriteState()
    object Empty : FavoriteState()
    data class Success(val characters: List<FavoriteCharacterEntity>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()
}
