package com.takehomechallenge.lillah.ui.search

import com.takehomechallenge.lillah.data.model.Character

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val data: List<Character>) : SearchState()
    object Empty : SearchState()
    data class Error(val message: String) : SearchState()
}