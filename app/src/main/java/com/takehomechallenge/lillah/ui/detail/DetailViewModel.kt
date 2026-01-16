package com.takehomechallenge.lillah.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.lillah.data.local.DatabaseProvider
import com.takehomechallenge.lillah.data.local.FavoriteCharacterEntity
import com.takehomechallenge.lillah.data.remote.ApiClient
import com.takehomechallenge.lillah.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.takehomechallenge.lillah.data.model.Character

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CharacterRepository

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state

    private val _favoriteState = MutableStateFlow<FavoriteActionState>(FavoriteActionState.Idle)
    val favoriteState: StateFlow<FavoriteActionState> = _favoriteState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    init {
        val db = DatabaseProvider.getDatabase(application)
        repository = CharacterRepository(ApiClient.api, db.favoriteDao())
    }

    fun fetchDetail(id: Int) {
        viewModelScope.launch {
            try {
                val character = repository.getCharacterDetail(id)
                _state.value = DetailState.Success(character)
                _isFavorite.value = repository.isFavorite(id)
            } catch (e: Exception) {
                _state.value = DetailState.Error("Failed to load detail")
            }
        }
    }

    fun toggleFavorite(character: Character) {
        viewModelScope.launch {
            _favoriteState.value = FavoriteActionState.Loading

            try {
                val entity = FavoriteCharacterEntity(
                    id = character.id,
                    name = character.name,
                    image = character.image
                )

                if (_isFavorite.value) {
                    repository.removeFavorite(entity)
                    _isFavorite.value = false
                    _favoriteState.value = FavoriteActionState.Removed
                } else {
                    repository.addFavorite(entity)
                    _isFavorite.value = true
                    _favoriteState.value = FavoriteActionState.Added
                }

            } catch (_: Exception) {
                _favoriteState.value = FavoriteActionState.Error("Failed to update favorite")
            }
        }
    }
}