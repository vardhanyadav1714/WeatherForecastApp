package com.discoverthe.weatherforecastapp.screens.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.discoverthe.weatherforecastapp.R
import com.discoverthe.weatherforecastapp.navigation.WeatherScreens
import com.discoverthe.weatherforecastapp.screens.settings.SettingsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherSplashScreen(navController: NavController, settingsViewModel: SettingsViewModel = koinViewModel()) {
    val defaultCity = "Seattle"

    val scale = remember { Animatable(0f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(key1 = true) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1200,
                easing = { OvershootInterpolator(2.5f).getInterpolation(it) }
            )
        )
        alpha.animateTo(1f, tween(1000))
        
        delay(2000L)
        navController.navigate(WeatherScreens.MainScreen.name + "/$defaultCity") {
            popUpTo(WeatherScreens.SplashScreen.name) { inclusive = true }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF000000), Color(0xFF141E30), Color(0xFF243B55))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // Enhanced Logo Container
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
                    .border(2.dp, Brush.linearGradient(listOf(Color.Cyan, Color.Transparent)), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sunny),
                    contentDescription = "Logo",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(110.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Discover",
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 6.sp
            )
            
            Text(
                text = "THE WEATHER",
                style = MaterialTheme.typography.titleMedium,
                color = Color.Cyan.copy(alpha = 0.7f),
                fontWeight = FontWeight.Light,
                letterSpacing = 10.sp
            )
            
            Spacer(modifier = Modifier.height(60.dp))
            
            Text(
                text = "PREMIUM EXPERIENCE",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.3f),
                letterSpacing = 2.sp
            )
        }
    }
}
