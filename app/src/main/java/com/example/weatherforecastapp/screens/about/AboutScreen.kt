package com.example.weatherforecastapp.screens.about

import WeatherAppBar
import android.annotation.SuppressLint
import android.support.v4.os.IResultReceiver.Default
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.weatherforecastapp.WeatherApp
import com.example.weatherforecastapp.navigation.WeatherScreens
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(topBar = {
        WeatherAppBar(title = "About",navController=navController, icon= Icons.Default.ArrowBack, isMainScreen = false){
            navController.popBackStack()
        }
    }) {
          Surface(modifier = Modifier
              .fillMaxWidth()
              .fillMaxHeight()) {
              Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center){
                  androidx.compose.material.Text(text = androidx.compose.ui.res.stringResource(id =com.example.weatherforecastapp.R.string.app_name)
                  , style = androidx.compose.material.MaterialTheme.typography.subtitle1, fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold)
                  androidx.compose.material.Text(text ="Weather API by: https://openweathermap.org"
                      , style = androidx.compose.material.MaterialTheme.typography.subtitle1, fontWeight = androidx.compose.ui.text.font.FontWeight.Companion.Bold)

              }
          }
    }
}
