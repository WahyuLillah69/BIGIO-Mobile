package com.takehomechallenge.lillah.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.takehomechallenge.lillah.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow<SearchState>(SearchState.Idle)
    val state: StateFlow<SearchState> = _state

    fun search(query: String) {
        if (query.isBlank()) {
            _state.value = SearchState.Empty
            return
        }

        viewModelScope.launch {
            _state.value = SearchState.Loading
            try {
                val result = repository.searchCharacter(query)
                _state.value =
                    if (result.isEmpty()) SearchState.Empty
                    else SearchState.Success(result)
            } catch (e: Exception) {
                _state.value = SearchState.Error("Character not found")
            }
        }
    }
}