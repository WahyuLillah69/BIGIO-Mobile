package com.takehomechallenge.lillah.ui.detail

import com.takehomechallenge.lillah.data.model.Character

sealed class DetailState {
    object Loading : DetailState()
    data class Success(val character: Character) : DetailState()
    data class Error(val message: String) : DetailState()
}

sealed class FavoriteActionState {
    object Idle : FavoriteActionState()
    object Loading : FavoriteActionState()
    object Added : FavoriteActionState()
    object Removed : FavoriteActionState()
    data class Error(val message: String) : FavoriteActionState()
}