package com.takehomechallenge.lillah.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.takehomechallenge.lillah.ui.detail.DetailScreen
import com.takehomechallenge.lillah.ui.favorite.FavoriteScreen
import com.takehomechallenge.lillah.ui.home.HomeScreen
import com.takehomechallenge.lillah.ui.search.SearchScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {

        composable("home") {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                },
                onFavoriteClick = {
                    navController.navigate("favorite")
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }

        composable("detail/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toInt() ?: return@composable
            DetailScreen(
                characterId = id,
                onBack = { navController.popBackStack() }
            )
        }

        composable("favorite") {
            FavoriteScreen(
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable("search") {
            SearchScreen(
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                },
                onBack = { navController.popBackStack() }
            )
        }
    }
}