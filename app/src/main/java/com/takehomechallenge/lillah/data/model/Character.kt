package com.takehomechallenge.lillah.data.model

data class Character(
    val id: Int,
    val name: String,
    val species: String,
    val gender: String,
    val image: String,
    val origin: Origin,
    val location: Location
)