package com.example.weatherforecastapp.screens.splash

import android.icu.number.Scale
import android.util.Log
import android.view.animation.OvershootInterpolator
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.weatherforecastapp.R

import com.example.weatherforecastapp.model.Unit
import com.example.weatherforecastapp.navigation.WeatherScreens
import com.example.weatherforecastapp.screens.settings.SettingsViewModel
import kotlinx.coroutines.delay

@Composable
fun WeatherSplashScreen(navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val defaultCity = "San Diego"

    settingsViewModel.insertUnit(Unit(unit = "imperial"))

    val backgroundAnim = rememberInfiniteTransition()
    val sunColorAnim = rememberInfiniteTransition()
    Log.d("SplashScreen", "Inside the weather splash screen ")
    val backgroundColor by backgroundAnim.animateColor(
        initialValue = Color(0xFFE1F5FE),
        targetValue = Color(0xFFFFC107),
        animationSpec = infiniteRepeatable(
            animation = tween(3000),
            repeatMode = RepeatMode.Reverse
        )
    )

    val sunColor by sunColorAnim.animateColor(
        initialValue = Color(0xFFFFC107),
        targetValue = Color(0xFFFFEB3B),
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor), // Background color animation
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_sun),
                contentDescription = "Sunny Icon",
                tint = sunColor, // Sun icon color animation
                modifier = Modifier.size(95.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Discover the Weather",
                style = MaterialTheme.typography.h5,
            )
        }
    }

    LaunchedEffect(key1 = true) {
        delay(2000L)
        Log.d("SplashScreen", "Before navigating to the mainscreen")

        navController.navigate(WeatherScreens.MainScreen.name + "/$defaultCity")
         Log.d("SplashScreen", "After navigating to the mainscreen")
    }
}
