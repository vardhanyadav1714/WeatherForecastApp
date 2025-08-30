package com.example.weatherforecastapp.screens.settings

import WeatherAppBar
import android.annotation.SuppressLint
import android.content.Context
import android.view.RoundedCorner
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavController, settingsViewModel: SettingsViewModel = hiltViewModel()) {
     val context= LocalContext.current
     var unitToggleState by remember {
          mutableStateOf(false)
     }
     val measurementUnits = listOf("Imperial (F)", "Metric (C)")
     val choiceFromDb = settingsViewModel.unitList.collectAsState().value
     val defaultChoice = if (choiceFromDb.isEmpty()) measurementUnits[0]
     else choiceFromDb[0].unit
     var choiceState by remember {
          mutableStateOf(defaultChoice)
     }

     Scaffold(
          topBar = {
               WeatherAppBar(
                    title = "Settings",
                    isMainScreen = false,
                    icon = Icons.Default.ArrowBack,
                    navController = navController
               ) {
                    navController.popBackStack()
               }
          }
     ) {
          Surface(
               modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
          ) {
               Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
               ) {
                    Text(text = "Change Units of Measurement", modifier = Modifier.padding(bottom = 15.dp))

                    Row(
                         verticalAlignment = Alignment.CenterVertically,
                         horizontalArrangement = Arrangement.spacedBy(8.dp),
                         modifier = Modifier.clickable { unitToggleState = !unitToggleState }
                    ) {
                         Text(text = "Imperial (F)", color = if (unitToggleState) Color.Gray else Color.Black)
                         Switch(
                              checked = unitToggleState,
                              onCheckedChange = { unitToggleState = it },
                              colors = SwitchDefaults.colors(
                                   checkedThumbColor = Color.White,
                                   checkedTrackColor = Color.Gray,
                                   uncheckedThumbColor = Color.White,
                                   uncheckedTrackColor = Color.Gray
                              )
                         )
                         Text(text = "Metric (C)", color = if (unitToggleState) Color.Black else Color.Gray)
                    }

                    Button(
                         onClick = {
                              // Update the choiceState based on the selected units
                              choiceState = if (unitToggleState) "Metric (C)" else "Imperial (F)"

                              // Save the selected units to the database
                              settingsViewModel.deleteAllUnits()
                              settingsViewModel.insertUnit(com.example.weatherforecastapp.model.Unit(unit = choiceState))

                              // Show a toast message indicating units have changed successfully


                              // Navigate back to the main screen
                              navController.navigateUp()
                         },
                         modifier = Modifier
                              .padding(3.dp)
                              .align(CenterHorizontally),
                         shape = RoundedCornerShape(34.dp),
                         colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEFBE42))
                    ) {
                         Text(
                              text = "Save",
                              modifier = Modifier.padding(4.dp),
                              color = Color.White,
                              fontSize = 17.sp,
                         )
                    }
               }
          }
     }
}

