package com.discoverthe.weatherforecastapp.screens.main

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.discoverthe.weatherforecastapp.R
import com.discoverthe.weatherforecastapp.data.DataOrException
import com.discoverthe.weatherforecastapp.model.AirQuality
import com.discoverthe.weatherforecastapp.model.ForecastDay
import com.discoverthe.weatherforecastapp.screens.favorites.FavoriteViewModel
import org.koin.androidx.compose.koinViewModel
import android.widget.Toast
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.ui.platform.LocalContext
import com.discoverthe.weatherforecastapp.model.WeatherResponse
import com.discoverthe.weatherforecastapp.navigation.WeatherScreens
import com.discoverthe.weatherforecastapp.screens.settings.SettingsViewModel
import com.discoverthe.weatherforecastapp.screens.splash.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel(),
    favoriteViewModel: FavoriteViewModel = koinViewModel(),
    city: String
) {
    val currCity = if (city.isBlank()) "Seattle" else city
    val unitList by settingsViewModel.unitList.collectAsState()
    var isImperial by remember { mutableStateOf(false) }

    if (unitList.isNotEmpty()) {
         isImperial = unitList[0].unit == "Imperial (F)"
    }

    val favorites = favoriteViewModel.favList.collectAsState().value
    val isFavorite = favorites.any { it.city.equals(currCity, ignoreCase = true) }
    val context = LocalContext.current

    val weatherData = produceState<DataOrException<WeatherResponse, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.getWeatherData(city = currCity)
    }.value

    val gradientBrush = remember(weatherData.data) {
        getGradientByWeather(weatherData.data?.current?.condition?.text ?: "", weatherData.data?.current?.is_day ?: 1)
    }

    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradientBrush)
        ) {
            if (weatherData.loading == true) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.White)
                }
            } else if (weatherData.data != null) {
                AdvancedMainContent(
                    data = weatherData.data!!,
                    navController = navController,
                    isImperial = isImperial,
                    isFavorite = isFavorite,
                    onFavoriteClick = {
                        if (isFavorite) {
                            val fav = favorites.first { it.city.equals(currCity, ignoreCase = true) }
                            favoriteViewModel.deleteFavorite(fav)
                            Toast.makeText(context, "Removed from Favorites", Toast.LENGTH_SHORT).show()
                        } else {
                            val fav = com.discoverthe.weatherforecastapp.model.Favorite(
                                city = weatherData.data!!.location.name,
                                country = weatherData.data!!.location.country
                            )
                            favoriteViewModel.insertFavorite(fav)
                            Toast.makeText(context, "Saved to Favorites", Toast.LENGTH_SHORT).show()
                        }
                    },
                    paddingValues = padding
                )
            } else if (weatherData.e != null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color.White, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Connection Error",
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = weatherData.e!!.localizedMessage ?: "Please try again later",
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 40.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdvancedMainContent(
    data: WeatherResponse,
    navController: NavController,
    isImperial: Boolean,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    paddingValues: PaddingValues
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Custom Modern Header
        // Custom Modern Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HeaderButton(Icons.Default.Search) { navController.navigate(WeatherScreens.SearchScreen.name) }
            
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.location.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(painterResource(R.drawable.ic_sun), contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.Yellow)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${data.location.region}, ${data.location.country}",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.6f),
                        maxLines = 1,
                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(12.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HeaderButton(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder) {
                    onFavoriteClick()
                }
                Spacer(modifier = Modifier.width(8.dp))
                HeaderButton(Icons.Default.List) { navController.navigate(WeatherScreens.FavoriteScreen.name) }
                Spacer(modifier = Modifier.width(8.dp))
                HeaderButton(Icons.Default.Settings) { navController.navigate(WeatherScreens.SettingsScreen.name) }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Hero Weather Display
        HeroWeatherDisplay(data, isImperial)

        Spacer(modifier = Modifier.height(32.dp))

        // Air Quality Section (ADVANCED FEATURE)
        AQICard(data.current.air_quality)

        Spacer(modifier = Modifier.height(24.dp))

        // Section Title: Detailed Forecast
        SectionHeader("3-Day Forecast")

        // Advanced Forecast Layout
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            data.forecast.forecastday.take(3).forEach { day ->
                DetailedForecastCard(day, isImperial)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Advanced Grid Stats
        SectionHeader("Atmospheric Details")
        AdvancedMetricsGrid(data, isImperial)

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun HeaderButton(icon: ImageVector, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = CircleShape,
        color = Color.White.copy(alpha = 0.15f),
        modifier = Modifier.size(44.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }
}

@Composable
fun HeroWeatherDisplay(data: WeatherResponse, isImperial: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val iconUrl = "https:${data.current.condition.icon}".replace("64x64", "128x128")
        
        Box(contentAlignment = Alignment.Center) {
            // Subtle glow background for icon
            Surface(
                modifier = Modifier.size(160.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.05f)
            ) {}
            
            Image(
                painter = rememberAsyncImagePainter(iconUrl),
                contentDescription = null,
                modifier = Modifier.size(140.dp)
            )
        }

        val temp = if (isImperial) data.current.temp_f else data.current.temp_c
        
        Text(
            text = "${temp.toInt()}°",
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 110.sp),
            fontWeight = FontWeight.Black,
            color = Color.White
        )
        
        Text(
            text = data.current.condition.text.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White.copy(alpha = 0.9f),
            letterSpacing = 4.sp,
            fontWeight = FontWeight.SemiBold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            val max = if(isImperial) data.forecast.forecastday[0].day.maxtemp_f else data.forecast.forecastday[0].day.maxtemp_c
            val min = if(isImperial) data.forecast.forecastday[0].day.mintemp_f else data.forecast.forecastday[0].day.mintemp_c
            
            Text("H: ${max.toInt()}°", color = Color.White, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.width(16.dp))
            Text("L: ${min.toInt()}°", color = Color.White.copy(alpha=0.6f), style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun AQICard(aqi: AirQuality) {
    val level = when(aqi.us_epa_index) {
        1 -> "Good"
        2 -> "Moderate"
        3 -> "Unhealthy for Sensitive Groups"
        4 -> "Unhealthy"
        5 -> "Very Unhealthy"
        6 -> "Hazardous"
        else -> "Unknown"
    }
    val levelColor = when(aqi.us_epa_index) {
        1 -> Color(0xFF4CAF50)
        2 -> Color(0xFFFFEB3B)
        3 -> Color(0xFFFF9800)
        4 -> Color(0xFFF44336)
        5 -> Color(0xFF9C27B0)
        6 -> Color(0xFF795548)
        else -> Color.White
    }

    GlassCard {
        Column(modifier = Modifier.padding(20.dp).fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Air Quality Index", style = MaterialTheme.typography.labelLarge, color = Color.White.copy(alpha=0.6f))
                    Text(
                        text = level, 
                        style = MaterialTheme.typography.titleLarge.copy(
                            shadow = androidx.compose.ui.graphics.Shadow(
                                color = Color.Black.copy(alpha = 0.5f),
                                blurRadius = 4f
                            )
                        ), 
                        fontWeight = FontWeight.Bold, 
                        color = levelColor
                    )
                }
                
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(levelColor.copy(alpha=0.25f)).border(1.dp, levelColor.copy(alpha=0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("${aqi.us_epa_index}", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Black, color = levelColor)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = Color.White.copy(alpha=0.1f))
            Spacer(modifier = Modifier.height(16.dp))
            
            // Detailed Pollutants Breakdown
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PollutantItem("CO", String.format("%.1f", aqi.co), Modifier.weight(1f))
                PollutantItem("NO₂", String.format("%.1f", aqi.no2), Modifier.weight(1f))
                PollutantItem("O₃", String.format("%.1f", aqi.o3), Modifier.weight(1f))
                PollutantItem("SO₂", String.format("%.1f", aqi.so2), Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PollutantItem("PM2.5", String.format("%.1f", aqi.pm2_5), Modifier.weight(1f))
                PollutantItem("PM10", String.format("%.1f", aqi.pm10), Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(2f)) // Balance the row
            }
        }
    }
}

@Composable
fun PollutantItem(name: String, value: String, modifier: Modifier) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(name, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha=0.5f))
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Composable
fun DetailedForecastCard(day: ForecastDay, isImperial: Boolean) {
    val maxTemp = if (isImperial) day.day.maxtemp_f else day.day.maxtemp_c
    val minTemp = if (isImperial) day.day.mintemp_f else day.day.mintemp_c
    val parsedDate = parseDate(day.date)
    
    GlassCard {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1.5f)) {
                Text(parsedDate.dayOfWeek, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(day.date, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha=0.5f))
            }
            
            Row(modifier = Modifier.weight(2f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                Image(
                    painter = rememberAsyncImagePainter("https:${day.day.condition.icon}"),
                    contentDescription = null,
                    modifier = Modifier.size(44.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(day.day.condition.text, style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha=0.8f), maxLines = 1)
            }
            
            Row(modifier = Modifier.weight(1.5f), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                Text("${maxTemp.toInt()}°", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color.White)
                Text(" / ${minTemp.toInt()}°", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha=0.4f))
            }
        }
    }
}

@Composable
fun AdvancedMetricsGrid(data: WeatherResponse, isImperial: Boolean) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard("UV Index", "${data.current.uv}", "Medium", Modifier.weight(1f))
            MetricCard("Humidity", "${data.current.humidity}%", "Comfortable", Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard("Visibility", if(isImperial) "${data.current.vis_miles} mi" else "${data.current.vis_km} km", "Clear", Modifier.weight(1f))
            MetricCard("Pressure", if(isImperial) "${data.current.pressure_in} in" else "${data.current.pressure_mb} mb", "Stable", Modifier.weight(1f))
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            MetricCard("Sunrise", data.forecast.forecastday[0].astro.sunrise, "AM", Modifier.weight(1f))
            MetricCard("Sunset", data.forecast.forecastday[0].astro.sunset, "PM", Modifier.weight(1f))
        }
    }
}

@Composable
fun MetricCard(label: String, value: String, desc: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color.White.copy(alpha=0.5f))
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(desc, style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha=0.4f))
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Color.White,
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp, top = 8.dp)
    )
}

@Composable
fun GlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.12f))
            .border(1.4.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
    ) {
        content()
    }
}

data class ParsedDate(val dayOfWeek: String)

fun parseDate(dateStr: String): ParsedDate {
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = format.parse(dateStr) ?: Date()
    val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
    return ParsedDate(dayFormat.format(date))
}

fun getGradientByWeather(condition: String, isDay: Int): Brush {
    val colors = when {
        condition.contains("Rain", ignoreCase = true) || condition.contains("Drizzle", ignoreCase = true) -> listOf(
            Color(0xFF203A43), Color(0xFF2C5364)
        )
        condition.contains("Cloud", ignoreCase = true) || condition.contains("Overcast", ignoreCase = true) -> {
             if (isDay == 1) listOf(Color(0xFF606c88), Color(0xFF3f4c6b))
             else listOf(Color(0xFF000000), Color(0xFF434343))
        }
        condition.contains("Snow", ignoreCase = true) -> listOf(
            Color(0xFFE0EAFC), Color(0xFFCFDEF3)
        )
        condition.contains("Sunny", ignoreCase = true) || condition.contains("Clear", ignoreCase = true) -> {
            if (isDay == 1) listOf(Color(0xFF4facfe), Color(0xFF00f2fe))
            else listOf(Color(0xFF09203f), Color(0xFF537895))
        }
        else -> listOf(Color(0xFF13547a), Color(0xFF80d0c7))
    }
    return Brush.verticalGradient(colors)
}
