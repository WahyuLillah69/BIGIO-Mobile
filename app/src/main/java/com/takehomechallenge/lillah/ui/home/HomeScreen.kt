package com.takehomechallenge.lillah.ui.home

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.takehomechallenge.lillah.data.local.DatabaseProvider
import com.takehomechallenge.lillah.data.remote.ApiClient
import com.takehomechallenge.lillah.data.repository.CharacterRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onItemClick: (Int) -> Unit,
    onFavoriteClick: () -> Unit,
    onSearchClick: () -> Unit
) {
    val context = LocalContext.current

    val repository = remember {
        CharacterRepository(
            ApiClient.api,
            DatabaseProvider
                .getDatabase(context.applicationContext as Application)
                .favoriteDao()
        )
    }

    val viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(repository)
    )

    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rick & Morty") },
                actions = {
                    IconButton(onClick = onSearchClick) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favorite"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            when (state) {
                is HomeState.Loading -> CircularProgressIndicator()

                is HomeState.Error ->
                    Text((state as HomeState.Error).message)

                is HomeState.Empty ->
                    Text("No character found")

                is HomeState.Success -> {
                    LazyColumn {
                        items((state as HomeState.Success).data) { character ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onItemClick(character.id) }
                                    .padding(16.dp)
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(character.image),
                                    contentDescription = null,
                                    modifier = Modifier.size(80.dp)
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(character.name)
                            }
                        }
                    }
                }
            }
        }
    }
}