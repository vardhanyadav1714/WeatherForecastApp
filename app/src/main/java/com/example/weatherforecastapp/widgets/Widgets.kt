package com.example.weatherforecastapp.widgets

import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.Coil
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.example.weatherforecastapp.R
import com.example.weatherforecastapp.model.WeatherItem
import com.example.weatherforecastapp.utils.formatDate
import com.example.weatherforecastapp.utils.formatDecimal
import com.example.weatherforecastapp.utils.formateDateTime


@Composable
fun WeatherDetailRow(weather: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weather.weather[0].icon}.png"

    Box(
        modifier = Modifier
            .width(250.dp) // Keep the width the same
            .height(180.dp) // Keep the height the same
            .padding(8.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(
                Brush.verticalGradient(
                    colors = getWeatherGradient(weather.weather[0].main)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(weather.dt).split(",")[0],
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                WeatherStateImage(weatherCondition = weather.weather[0].main, modifier = Modifier.height(80.dp))
            }

            Surface(
                modifier = Modifier.padding(8.dp),
                shape = CircleShape,
                color = getWeatherColor(weather.weather[0].main)
            ) {
                Text(
                    weather.weather[0].description,
                    style = MaterialTheme.typography.caption.copy(color = Color.White),
                    modifier = Modifier.padding(8.dp)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(formatDecimal(weather.temp.max) + "°")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.LightGray.copy(alpha = 0.7f),
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append(" / ${formatDecimal(weather.temp.min)}°")
                        }
                    },
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Composable
fun getWeatherGradient(weatherMain: String): List<Color> {
    return when (weatherMain) {
        "Clear" -> listOf(Color(0xFF64B5F6), Color(0xFF1976D2)) // Gradient for clear sky
        "Clouds" -> listOf(Color(0xFFB0BEC5), Color(0xFF78909C)) // Gradient for cloudy sky
        "Rain", "Drizzle" -> listOf(Color(0xFF607D8B), Color(0xFF455A64)) // Gradient for rainy weather
        "Thunderstorm" -> listOf(Color(0xFF9E9D24), Color(0xFF827717)) // Gradient for thunderstorm
        "Snow" -> listOf(Color(0xFFE0E0E0), Color(0xFF90A4AE)) // Gradient for snowy weather
        else -> listOf(Color(0xFF64B5F6), Color(0xFF1976D2)) // Default gradient for other conditions
    }
}

@Composable
fun getWeatherColor(weatherMain: String): Color {
    return when (weatherMain) {
        "Clear" -> Color(0xFFFFC400) // Yellow for clear sky
        "Clouds" -> Color(0xFFB0BEC5) // Gray for cloudy sky
        "Rain", "Drizzle" -> Color(0xFF2196F3) // Blue for rainy weather
        "Thunderstorm" -> Color(0xFFFF6F00) // Amber for thunderstorm
        "Snow" -> Color(0xFFFFFFFF) // White for snowy weather
        else -> Color(0xFF64B5F6) // Default color for other conditions
    }
}


@Composable
fun SunsetAndSunshineRow(weatherItem: WeatherItem) {
    Row(
        modifier = Modifier
            .padding(vertical = 16.dp)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB0BEC5).copy(alpha = 0.5f), // Start color with transparency
                        Color(0xFF78909C).copy(alpha = 0.5f)  // End color with transparency
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        WeatherDetailItem(
            iconId = R.drawable.ic_sunrise,
            label = "Sunrise",
            value = formateDateTime(weatherItem.sunrise),
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        WeatherDetailItem(
            iconId = R.drawable.ic_sunset,
            label = "Sunset",
            value = formateDateTime(weatherItem.sunset),
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
        )
    }
}

@Composable
fun HumidityWindPressureRow(weatherItem: WeatherItem, isImperial: Boolean) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB0BEC5).copy(alpha = 0.5f),
                        Color(0xFF78909C).copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        WeatherDetailItem(
            iconId = R.drawable.ic_humidity,
            label = "Humidity",
            value = "${weatherItem.humidity}%",
            modifier = Modifier.weight(1f)
        )
        WeatherDetailItem(
            iconId = R.drawable.ic_pressure,
            label = "Pressure",
            value = getPressureValue(weatherItem.pressure.toDouble(), isImperial),
            modifier = Modifier.weight(1f)
        )
        WeatherDetailItem(
            iconId = R.drawable.ic_wind_speed,
            label = "Wind",
            value = "${formatWindSpeed(weatherItem.speed, isImperial)}",
            modifier = Modifier.weight(1f)
        )
    }
}

private fun getPressureValue(pressure: Double, isImperial: Boolean): String {
    return if (isImperial) {
        val psiValue = pressure * 0.0145038 // Conversion from hPa to psi
        "${formatDecimal(psiValue)} psi"
    } else {
        "${formatDecimal(pressure)} hPa"
    }
}
private fun formatWindSpeed(speed: Double, isImperial: Boolean): String {
    return if (isImperial) {
        "${formatDecimal(speed)} mph"
    } else {
        "${formatDecimal(speed)} m/s"
    }
}

@Composable
fun WeatherDetailItem(iconId: Int, label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFB0BEC5).copy(alpha = 0.7f), // Start color with transparency
                        Color(0xFF78909C).copy(alpha = 0.7f)  // End color with transparency
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "$label icon",
            modifier = Modifier
                .size(30.dp)
                .padding(8.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = Color.Gray
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}
@Composable
fun WeatherStateImage(weatherCondition: String, modifier: Modifier) {
    val imageUrl = when (weatherCondition) {
        "Clear" -> R.drawable.sunny
        "Clouds" -> R.drawable.clouds
        "Rain", "Drizzle" -> R.drawable.rain
        "Thunderstorm" -> R.drawable.thunderstorm
        "Snow" -> R.drawable.snowfall
        "Wind" -> R.drawable.wind
        else -> R.drawable.sunny
    }

    Image(
        painter = rememberImagePainter(data = imageUrl),
        contentDescription = "Weather image",
        modifier = modifier
    )
}
