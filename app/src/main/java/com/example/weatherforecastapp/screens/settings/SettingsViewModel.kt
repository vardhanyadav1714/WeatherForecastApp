package com.example.weatherforecastapp.screens.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherforecastapp.model.Favorite
import com.example.weatherforecastapp.repositary.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel  @Inject constructor(private val repository: WeatherDbRepository):
    ViewModel() {
    private val _unitList = MutableStateFlow<List<com.example.weatherforecastapp.model.Unit>>(emptyList())
    val unitList = _unitList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUnits().distinctUntilChanged().collect { listOfUnit->
                if (listOfUnit.isEmpty()) {
                    Log.d("TAG", "List is null and empty")
                } else {
                    _unitList.value = listOfUnit
                    Log.d("FAVS", ":${unitList.value} ")
                }
            }
        }
    }

    fun insertUnit(unit: com.example.weatherforecastapp.model.Unit) =
        viewModelScope.launch { repository.insertUnit(unit) }

    fun updateUnit(unit: com.example.weatherforecastapp.model.Unit) = viewModelScope.launch {
        repository.updateUnit(unit)
    }

    fun deleteUnit(unit: com.example.weatherforecastapp.model.Unit) = viewModelScope.launch {
        repository.deleteUnit(unit)

    }
    fun deleteAllUnits() = viewModelScope.launch {
        repository.deleteAllUnits()
    }
}
