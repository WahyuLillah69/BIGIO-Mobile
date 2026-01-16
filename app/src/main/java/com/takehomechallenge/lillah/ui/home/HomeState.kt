package com.takehomechallenge.lillah.ui.home

import com.takehomechallenge.lillah.data.model.Character

sealed class HomeState {
    object Loading : HomeState()
    data class Success(val data: List<Character>) : HomeState()
    object Empty : HomeState()
    data class Error(val message: String) : HomeState()
}