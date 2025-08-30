package com.example.weatherforecastapp.screens.main

import WeatherAppBar
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecastapp.data.DataOrException
import com.example.weatherforecastapp.model.Weather
import com.example.weatherforecastapp.model.WeatherItem
import com.example.weatherforecastapp.navigation.WeatherScreens
import com.example.weatherforecastapp.screens.settings.SettingsViewModel
import com.example.weatherforecastapp.screens.splash.MainViewModel
import com.example.weatherforecastapp.utils.formatDate
import com.example.weatherforecastapp.utils.formatDecimal
import com.example.weatherforecastapp.widgets.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    settingsViewModel: SettingsViewModel= hiltViewModel(),
    city: String
){
    val currCity:String = if(city.isBlank()) "Seattle" else city
    val unitFromDb = settingsViewModel.unitList.collectAsState().value
    var unit by remember {
        mutableStateOf("imperial")
    }
    var isImperial by remember{
        mutableStateOf(false)
    }
    if(unitFromDb.isNotEmpty()){
        unit=unitFromDb[0].unit.split(" ")[0].lowercase()
        isImperial=unit=="imperial"
        val weatherData= produceState<DataOrException<Weather,Boolean,Exception>>(initialValue = DataOrException(loading = true)){
            value=mainViewModel.getWeatherData(city=currCity,units=unit)
        }.value
        if(weatherData.loading==true){
            CircularProgressIndicator()
        }
        else if(weatherData.data!=null){
            MainScaffold(weather=weatherData.data!!,navController,isImperial=isImperial)
        }
    }
//    else{
//        Text(text="Main Screen")
//    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScaffold(weather: Weather, navController: NavController, isImperial: Boolean){
     Scaffold(topBar = { WeatherAppBar(title = weather.city.name +",${weather.city.country}", navController = navController, onAddActionClicked = {
                                  navController.navigate(WeatherScreens.SearchScreen.name)

     }, elevation = 5.dp)})  {
         MainContent(data=weather,isImperial)
     }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun MainContent(data: Weather, isImperial: Boolean) {
    val weatherItem = data.list[0]
    val imageUrl="https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

        Column(
            Modifier
                .padding(4.dp)
                .fillMaxWidth().verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = formatDate(weatherItem.dt), // Wed Nov 30
                style = MaterialTheme.typography.caption,
                color = MaterialTheme.colors.onSecondary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(6.dp))

            Surface(modifier = Modifier
                .padding(4.dp)
                .size(200.dp),
                shape = CircleShape,
                color = Color(0xFFFFC400)) {

                Column(verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    WeatherStateImage(weatherCondition = weatherItem.weather[0].main, modifier = Modifier.height(80.dp))

                    Text(text = formatDecimal(weatherItem.temp.day) + "º",
                        style = MaterialTheme.typography.h3,
                        fontWeight = FontWeight.ExtraBold)
                    Text(text = weatherItem.weather[0].main,
                       fontStyle = FontStyle.Italic)
                }
            }

            HumidityWindPressureRow(weatherItem,isImperial=isImperial)
            Divider()
            SunsetAndSunshineRow(weatherItem)
            Text(text = "This Week",style=MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
            Surface(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),color=Color(0xFFEEF1EF), shape = RoundedCornerShape(size = 14.dp)) {
                LazyRow(
                    modifier = Modifier
                        .padding(2.dp)
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = data.list) { item: WeatherItem ->
                        WeatherDetailRow(weather = item)
                    }
                }


            }
        }
}
