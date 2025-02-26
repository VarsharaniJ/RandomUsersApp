package com.example.randomuserapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.randomuserapp.presentation.screens.Screen
import com.example.randomuserapp.presentation.screens.UserDetailScreen
import com.example.randomuserapp.presentation.screens.UserListScreen
import com.example.randomuserapp.presentation.viewmodels.UserListViewModel

@Composable
fun NavigationGraph(navController: NavHostController, userViewModel : UserListViewModel = viewModel()) {
    NavHost(navController = navController, startDestination = Screen.USER_LIST_SCREEN) {
        composable(Screen.USER_LIST_SCREEN) {
            UserListScreen(navController, userViewModel)
        }
        composable(
            Screen.USER_DETAILS_SCREEN+"/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ) { backStackEntry ->
            val index = backStackEntry.arguments?.getInt("index") ?: -1
            UserDetailScreen(index, userViewModel, navController)
        }
    }
}