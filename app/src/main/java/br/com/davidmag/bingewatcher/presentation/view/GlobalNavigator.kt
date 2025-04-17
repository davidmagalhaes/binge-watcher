package br.com.davidmag.bingewatcher.presentation.view

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.com.davidmag.bingewatcher.presentation.view.theme.AppTheme
import br.com.davidmag.bingewatcher.presentation.viewmodel.HomeViewModel

@Composable
fun GlobalNavigator() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = hiltViewModel()

    AppTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.HOME.value
        ) {
            composable(Screen.HOME.value) {
                HomeScreen(
                    homeViewModel.query,
                    homeViewModel.favoriteState,
                    homeViewModel.shows,
                    homeViewModel.errors,
                    homeViewModel::updateShows,
                    homeViewModel::showFavoritesClick,
                    { navController.navigate(Screen.EPISODE.value) },
                    homeViewModel::onSearchChange,
                    homeViewModel::submitSearch
                )
            }
            composable(Screen.EPISODE.value) {
                EpisodeScreen()
            }
            composable(Screen.SHOW.value) {
                ShowScreen()
            }
        }
    }
}