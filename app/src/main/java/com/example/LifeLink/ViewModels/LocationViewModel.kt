package com.example.LifeLink.ViewModels

import android.content.Context
import android.location.Location
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.LifeLink.Screens.getLastUserLocation
import com.example.LifeLink.Service.LocationTracker
import com.example.LifeLink.Utils.LatandLong
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationTracker: LocationTracker
) : ViewModel(){

    //---- get Current location
    // Now we have to create a variable that will hold the current location state and it will be updated with the getCurrentLocation function.

    var currentLocation by mutableStateOf<Location?>(null)
    var locationText by mutableStateOf("")

    init {
        viewModelScope.launch {
            currentLocation = locationTracker.getCurrentLocation()
        }
    }

    fun getCurrentLocation(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
//            currentLocation = locationTracker.getCurrentLocation() // Location
            locationText = getLastUserLocation(context, LatandLong(latitude =  currentLocation?.latitude ?:0.0, longitude = currentLocation?.longitude ?:0.0))
        }
    }
}