package com.takehomechallenge.lillah.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.lillah.data.local.DatabaseProvider
import com.takehomechallenge.lillah.data.local.FavoriteCharacterEntity
import com.takehomechallenge.lillah.data.remote.ApiClient
import com.takehomechallenge.lillah.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository

    private val _state = MutableStateFlow<FavoriteState>(FavoriteState.Loading)
    val state: StateFlow<FavoriteState> = _state

    init {
        val db = DatabaseProvider.getDatabase(application)
        repository = CharacterRepository(ApiClient.api, db.favoriteDao())
        loadFavorites()
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            repository.getFavorites().collectLatest { favorites ->
                _state.value =
                    if (favorites.isEmpty()) FavoriteState.Empty
                    else FavoriteState.Success(favorites)
            }
        }
    }

    fun removeFavorite(character: FavoriteCharacterEntity) {
        viewModelScope.launch {
            repository.removeFavorite(character)
        }
    }
}