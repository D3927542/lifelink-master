package com.example.LifeLink.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.LifeLink.Models.User
import com.example.LifeLink.R
import com.example.LifeLink.Utils.LatandLong
import com.example.LifeLink.ViewModels.LocationViewModel
import com.example.LifeLink.ui.theme.AppRed
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.Locale

@Composable
fun HomeScreen(user: User, locationViewModel: LocationViewModel = hiltViewModel(), rememberNavController: NavHostController) {
    val ctx = LocalContext.current
    LaunchedEffect(true){
        snapshotFlow {
            locationViewModel.currentLocation
        }
            .distinctUntilChanged()
            .onEach { query ->
                if(locationViewModel.locationText.isBlank())
                    locationViewModel.getCurrentLocation(ctx)
            }
            .launchIn(this)
    }
    Log.d("location_tag_lat", "${ locationViewModel.currentLocation?.latitude}")
    Log.d("location_tag_long", "${ locationViewModel.currentLocation?.longitude}")
//    var location =
//    Log.d("location_tag", "${location}")

    Column(modifier = Modifier
        .fillMaxSize()
        .paint(
            // Replace with your image id
            painterResource(id = R.drawable.map),
            contentScale = ContentScale.FillBounds
        ),
        verticalArrangement = Arrangement.SpaceAround
        ) {
     //name section
        GetUserDetail(user, rememberNavController)
        GetEmergencyText()
        //Spacer(modifier = Modifier.height(48.dp))
        Image(
            painter = painterResource(R.drawable.alertlatest),
            contentDescription = "alert",
            contentScale = ContentScale.Fit,            // crop the image if it's not a square
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { rememberNavController.navigate("help") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        GetLocation(user, locationViewModel.locationText)
    }
}

@Composable
fun GetUserDetail(user: User, rememberNavController : NavHostController){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(16.dp)
            .background(Color.White)
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Welcome Back",
                fontSize = 12.sp,
                color = Color(0xFFB2B3B5),
            )
            Text(
                text = user.name,
                fontSize = 16.sp,
                color = Color.Black
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd
        ) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(user.image, 0, user.image.size).asImageBitmap(),
                contentDescription = "display",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                    .size(48.dp)
                    .clickable { rememberNavController.navigate("settings") }
            )
        }
    }
}

@Composable
fun GetEmergencyText(){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Having an emergency?",
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Press the button below help will arrive soon",
            fontSize = 16.sp,
            color = Color(0xFFB2B3B5)
        )
    }
}

@Composable
fun GetLocation(user: User, currentUserLocation: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .size(48.dp)
                .align(Alignment.CenterVertically),
            contentAlignment = Alignment.CenterStart
        ) {
            Image(
                bitmap = BitmapFactory.decodeByteArray(user.image, 0, user.image.size).asImageBitmap(),
                contentDescription = "display",
                contentScale = ContentScale.Crop,            // crop the image if it's not a square
                modifier = Modifier
                    .clip(CircleShape)                       // clip to the circle shape
                    .border(2.dp, AppRed, CircleShape)   // add a border (optional)
                    .size(48.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = "Your Current Location",
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = currentUserLocation,
                fontSize = 12.sp,
                color = Color(0xFFB2B3B5),
                modifier = Modifier.padding(2.dp)
            )

        }

    }
}

@SuppressLint("MissingPermission")
fun getLastUserLocation(context: Context, currentUserLocation : LatandLong
):String {
    var locationText by mutableStateOf("Fetching Location...")
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses = geocoder.getFromLocation(currentUserLocation.latitude, currentUserLocation.longitude, 1)
    if (addresses!!.isNotEmpty()) {
        val address = addresses[0]
        locationText = address.getAddressLine(0)
    } else {
        locationText = ""
    }
    return locationText
}