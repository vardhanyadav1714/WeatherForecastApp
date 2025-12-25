package com.discoverthe.weatherforecastapp.navigation

import androidx.compose.runtime.Composable
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.discoverthe.weatherforecastapp.screens.about.AboutScreen
import com.discoverthe.weatherforecastapp.screens.favorites.FavoriteScreen
import com.discoverthe.weatherforecastapp.screens.settings.SettingsScreen
import com.discoverthe.weatherforecastapp.screens.main.MainScreen
import com.discoverthe.weatherforecastapp.screens.search.SearchScreen
import com.discoverthe.weatherforecastapp.screens.splash.MainViewModel
import com.discoverthe.weatherforecastapp.screens.splash.WeatherSplashScreen

@Composable
fun WeatherNavigation() {
    val navController= rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name){
        composable(WeatherScreens.SplashScreen.name){
            WeatherSplashScreen(navController=navController)
        }
        val route=WeatherScreens.MainScreen.name
        composable("$route/{city}", arguments = listOf(navArgument(name="city"){
            type= NavType.StringType
        })){navBack->
            navBack.arguments?.getString("city").let{city->
                val mainViewModel: MainViewModel = koinViewModel()
                if(city!=null) {
                    MainScreen(navController = navController, mainViewModel, city = city)
                }
            }

        }

        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }
        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController=navController)
        }
        composable(WeatherScreens.FavoriteScreen.name){
            FavoriteScreen(navController=navController)
        }
        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController=navController)
        }
    }

}
