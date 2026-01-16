# BIGIO Mobile

## Introduction :

BIGIO Mobile is an Android application built as part of a take-home challenge.
The app consumes data from the Rick & Morty public API and displays character information using a modern Android development approach.
This project is developed using **Jetpack Compose** and follows **MVVM architecture** to ensure clean separation of concerns, readability, and scalability.

---

## Table of Contents

* Introduction
* Features
* Libraries
* Project Structure
* APK Link

---

## Features :

* Display list of Rick & Morty characters
* View character detail information
* Search characters
* Add and remove characters to/from favorite
* Local data persistence using Room Database
* Loading and error state handling

---

## Libraries :

* **Kotlin**
* **Jetpack Compose (Material 3)**
* **Navigation Compose**
* **ViewModel**
* **StateFlow**
* **Coroutines**
* **Retrofit** (API Client)
* **Coil** (Image Loading)
* **Room Database**
* **JUnit** (Unit Testing)

---

## Project Structure :

```
com.takehomechallenge.lillah
/data
	/local
		AppDatabase.kt
		DatabaseProvider.kt
		FavoriteCharacterEntity.kt
		FavoriteDao.kt
	/model
		Character.kt
		CharacterResponse.kt
		Location.kt
		Origin.kt
	/remote
		ApiClient.kt
		RickMortyApi.kt
	/repository
		CharacterRepository.kt
/navigation
	NavGrapht.kt
/ui
	/detail
		DetailScreen.kt
		DetailState.kt
		DetailViewModel.kt
	/favorite
		FavoriteScreen.kt
		FavoriteState.kt
		FavoriteViewModel.kt
	/home
		HomeScreen.kt
		HomeState.kt
		HomeViewModel.kt
		HomeViewModelFactory.kt
	/search
		SearchScreen.kt
		SearchState.kt
		SearchViewModel.kt
		SearchviewModelFactory.kt	
	/theme
		Color.kt
		Theme.kt
		Type.kt
/util
	Constants.kt
	Extensions.kt
	UiState.kt
MainActivity.kt
```

---

## APK Link :

Drive: https://drive.google.com/drive/folders/1BHLpIliYW2gVUu3WwfL1MhWGb9VJwvLE?usp=sharing
