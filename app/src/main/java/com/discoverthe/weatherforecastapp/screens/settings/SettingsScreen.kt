package com.discoverthe.weatherforecastapp.screens.settings

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = koinViewModel()) {
     val choiceFromDb = settingsViewModel.unitList.collectAsState().value
     val defaultUnit = if (choiceFromDb.isEmpty()) "Imperial (F)" else choiceFromDb[0].unit
     
     // Local state to track the switch
     var isMetric by remember(defaultUnit) {
          mutableStateOf(defaultUnit == "Metric (C)")
     }

     Scaffold(
          containerColor = Color.Transparent
     ) { padding ->
          Box(
               modifier = Modifier
                    .fillMaxSize()
                    .background(
                         Brush.verticalGradient(
                              colors = listOf(Color(0xFF232526), Color(0xFF414345))
                         )
                    )
                    .padding(padding)
          ) {
               Column(
                    modifier = Modifier
                         .fillMaxSize()
                         .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
               ) {
                    // Header
                    Row(
                         modifier = Modifier
                              .fillMaxWidth()
                              .padding(top = 20.dp, bottom = 40.dp),
                         verticalAlignment = Alignment.CenterVertically
                    ) {
                         IconButton(onClick = { navController.popBackStack() }) {
                              Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                         }
                         Text(
                              text = "Settings",
                              style = MaterialTheme.typography.headlineSmall,
                              fontWeight = FontWeight.Bold,
                              color = Color.White,
                              modifier = Modifier.padding(start = 8.dp)
                         )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                         text = "Preferences",
                         style = MaterialTheme.typography.titleMedium,
                         color = Color.White.copy(alpha = 0.5f),
                         modifier = Modifier.align(Alignment.Start).padding(bottom = 12.dp)
                    )

                    // Unit Selection Card
                    Box(
                         modifier = Modifier
                              .fillMaxWidth()
                              .clip(RoundedCornerShape(24.dp))
                              .background(Color.White.copy(alpha = 0.05f))
                              .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(24.dp))
                              .clickable { isMetric = !isMetric }
                              .padding(24.dp)
                    ) {
                         Row(
                              verticalAlignment = Alignment.CenterVertically,
                              horizontalArrangement = Arrangement.SpaceBetween,
                              modifier = Modifier.fillMaxWidth()
                         ) {
                              Column {
                                   Text(
                                        text = "Units of Measurement",
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                   )
                                   Text(
                                        text = if (isMetric) "Metric (Celsius)" else "Imperial (Fahrenheit)",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.6f)
                                   )
                              }
                              
                              Switch(
                                   checked = isMetric,
                                   onCheckedChange = { isMetric = it },
                                   colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = Color.Cyan,
                                        uncheckedThumbColor = Color.Gray,
                                        uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                                   )
                              )
                         }
                    }

                    Spacer(modifier = Modifier.height(40.dp))

                    // Save Button
                    Button(
                         onClick = {
                              val choice = if (isMetric) "Metric (C)" else "Imperial (F)"
                              settingsViewModel.deleteAllUnits()
                              settingsViewModel.insertUnit(com.discoverthe.weatherforecastapp.model.Unit(unit = choice))
                              navController.popBackStack()
                         },
                         modifier = Modifier
                              .fillMaxWidth()
                              .height(56.dp),
                         shape = RoundedCornerShape(28.dp),
                         colors = ButtonDefaults.buttonColors(
                              containerColor = Color.Cyan,
                              contentColor = Color.Black
                         )
                    ) {
                         Text(
                              text = "Save Preferences",
                              style = MaterialTheme.typography.titleMedium,
                              fontWeight = FontWeight.Bold
                         )
                    }
               }
          }
     }
}
