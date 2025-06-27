package com.santucci.searchappdesafio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.santucci.searchappdesafio.presentation.detail.DetailScreen
import com.santucci.searchappdesafio.presentation.detail.DetailViewModel
import com.santucci.searchappdesafio.presentation.result.ResultsScreen
import com.santucci.searchappdesafio.presentation.result.ResultViewModel
import com.santucci.searchappdesafio.presentation.search.SearchScreen
import com.santucci.searchappdesafio.ui.theme.SearchappdesafioTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchAppActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchappdesafioTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "search"
    ) {

        composable("search") {
            SearchScreen(navController = navController)
        }

        composable(
            route = "results/{query}",
            arguments = listOf(navArgument("query") { type = NavType.StringType })
        ) { backStackEntry ->
            val query = backStackEntry.arguments?.getString("query") ?: ""
            val viewModel: ResultViewModel = hiltViewModel()
            viewModel.search(query)

            ResultsScreen(
                viewModel = viewModel,
                onProductClick = { itemId ->
                    navController.navigate("details/$itemId")
                }
            )
        }

        composable(
            route = "details/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable
            val viewModel: DetailViewModel = hiltViewModel()
            viewModel.loadProduct(itemId)

            DetailScreen(
                viewModel = hiltViewModel(),
                navController = navController
            )
        }
    }
}
