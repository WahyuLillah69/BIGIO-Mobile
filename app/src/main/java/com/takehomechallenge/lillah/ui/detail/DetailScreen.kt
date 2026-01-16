package com.takehomechallenge.lillah.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    characterId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {

    LaunchedEffect(characterId) {
        viewModel.fetchDetail(characterId)
    }

    val state by viewModel.state.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    val favoriteState by viewModel.favoriteState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        when (state) {
            is DetailState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is DetailState.Error -> {
                Text(
                    text = (state as DetailState.Error).message,
                    modifier = Modifier.padding(16.dp)
                )
            }

            is DetailState.Success -> {
                val c = (state as DetailState.Success).character

                Column(
                    modifier = Modifier
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(c.image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    Text("Name: ${c.name}")
                    Text("Species: ${c.species}")
                    Text("Gender: ${c.gender}")
                    Text("Origin: ${c.origin.name}")
                    Text("Location: ${c.location.name}")

                    Spacer(Modifier.height(24.dp))

                    if (!isFavorite) {
                        Button(
                            onClick = { viewModel.toggleFavorite(c) },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = favoriteState !is FavoriteActionState.Loading
                        ) {
                            when (favoriteState) {
                                FavoriteActionState.Loading -> {
                                    CircularProgressIndicator(
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(20.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                                else -> {
                                    Text("Add to Favorite")
                                }
                            }
                        }
                    }

                    if (favoriteState is FavoriteActionState.Added || isFavorite) {
                        Text(
                            text = "Added to favorite",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    if (favoriteState is FavoriteActionState.Error) {
                        Text(
                            text = (favoriteState as FavoriteActionState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}