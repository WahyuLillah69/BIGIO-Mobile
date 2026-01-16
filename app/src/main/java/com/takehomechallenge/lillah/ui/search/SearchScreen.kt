package com.takehomechallenge.lillah.ui.search

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.takehomechallenge.lillah.data.local.DatabaseProvider
import com.takehomechallenge.lillah.data.remote.ApiClient
import com.takehomechallenge.lillah.data.repository.CharacterRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onItemClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    // Repository dibuat sekali (clean & safe)
    val repository = remember {
        CharacterRepository(
            ApiClient.api,
            DatabaseProvider
                .getDatabase(context.applicationContext as Application)
                .favoriteDao()
        )
    }

    // ViewModel WAJIB pakai factory
    val viewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory(repository)
    )

    var query by remember { mutableStateOf("") }
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search character") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.search(query) },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Search")
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (state) {
                is SearchState.Loading -> {
                    CircularProgressIndicator()
                }

                is SearchState.Empty -> {
                    Text("No result")
                }

                is SearchState.Error -> {
                    Text((state as SearchState.Error).message)
                }

                is SearchState.Success -> {
                    LazyColumn {
                        items((state as SearchState.Success).data) { character ->
                            Text(
                                text = character.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onItemClick(character.id) }
                                    .padding(12.dp)
                            )
                        }
                    }
                }

                SearchState.Idle -> Unit
            }
        }
    }
}