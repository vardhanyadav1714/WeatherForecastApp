package com.discoverthe.weatherforecastapp.screens.favorites

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import androidx.navigation.NavController
import com.discoverthe.weatherforecastapp.model.Favorite as FavoriteModel
import com.discoverthe.weatherforecastapp.navigation.WeatherScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriteScreen(navController: NavController, favoriteViewModel: FavoriteViewModel = koinViewModel()) {
    Scaffold(
        containerColor = Color.Transparent
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F2027), Color(0xFF203A43), Color(0xFF2C5364))
                    )
                )
                .padding(padding)
        ) {
            val list = favoriteViewModel.favList.collectAsState().value
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp, bottom = 30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                    Text(
                        text = "Your Collection",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                if (list.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = Color.White.copy(alpha = 0.1f), modifier = Modifier.size(100.dp))
                            Text("No favorites saved yet", color = Color.White.copy(alpha = 0.4f), style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(bottom = 30.dp)
                    ) {
                        items(items = list) {
                            AdvancedCityRow(it, navController = navController, favoriteViewModel = favoriteViewModel)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdvancedCityRow(favorite: FavoriteModel, navController: NavController, favoriteViewModel: FavoriteViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(28.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.2.dp, Color.White.copy(alpha = 0.15f), RoundedCornerShape(28.dp))
            .clickable { navController.navigate(WeatherScreens.MainScreen.name + "/${favorite.city}") }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.city,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = favorite.country,
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Cyan.copy(alpha = 0.6f),
                    letterSpacing = 1.sp
                )
            }
            
            IconButton(
                onClick = { favoriteViewModel.deleteFavorite(favorite) },
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.Red.copy(alpha = 0.15f))
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color(0xFFFF5252),
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
