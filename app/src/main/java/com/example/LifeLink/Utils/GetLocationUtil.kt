package com.example.LifeLink.Utils

import android.Manifest
import android.content.Context
import android.content.IntentSender
import androidx.activity.result.IntentSenderRequest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.LifeLink.Constants.Permissions.Companion.allPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.SettingsClient
import com.google.android.gms.tasks.Task

//A callback for receiving notifications from the FusedLocationProviderClient.
lateinit var locationCallback: LocationCallback
//The main entry point for interacting with the Fused Location Provider
lateinit var locationProvider: FusedLocationProviderClient
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestAllPermission(context : Context
) {
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        allPermissions.toList()
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }

        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty())
            permissionsToRequest.forEach { it.launchPermissionRequest() }

        // Execute callbacks based on permission status.
//        if (!allPermissionsRevoked && !permissionState.allPermissionsGranted) {
//            Toast.makeText(context, "All permission required.", Toast.LENGTH_SHORT).show()
//        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getContactPermission(){
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.READ_CONTACTS)
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }
        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty())
            permissionsToRequest.forEach { it.launchPermissionRequest() }

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getLocationPermission(){
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf( Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,Manifest.permission.INTERNET,)
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }
        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty())
            permissionsToRequest.forEach { it.launchPermissionRequest() }

    }
}
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun getSmsPermission(){
    // Initialize the state for managing multiple location permissions.
    val permissionState = rememberMultiplePermissionsState(
        listOf( Manifest.permission.SEND_SMS)
    )

    // Use LaunchedEffect to handle permissions logic when the composition is launched.
    LaunchedEffect(key1 = permissionState) {
        // Check if all previously granted permissions are revoked.
        val allPermissionsRevoked =
            permissionState.permissions.size == permissionState.revokedPermissions.size

        // Filter permissions that need to be requested.
        val permissionsToRequest = permissionState.permissions.filter {
            !it.status.isGranted
        }
        // If there are permissions to request, launch the permission request.
        if (permissionsToRequest.isNotEmpty())
            permissionsToRequest.forEach { it.launchPermissionRequest() }

    }
}
data class LatandLong(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)

// call this function on button click
fun checkLocationSetting(
    context: Context,
    onDisabled: (IntentSenderRequest) -> Unit,
    onEnabled: () -> Unit
) {

    val locationRequest = LocationRequest.create().apply {
        interval = 1000
        fastestInterval = 1000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    val client: SettingsClient = LocationServices.getSettingsClient(context)
    val builder: LocationSettingsRequest.Builder = LocationSettingsRequest
        .Builder()
        .addLocationRequest(locationRequest)

    val gpsSettingTask: Task<LocationSettingsResponse> =
        client.checkLocationSettings(builder.build())

    gpsSettingTask.addOnSuccessListener { onEnabled() }
    gpsSettingTask.addOnFailureListener { exception ->
        if (exception is ResolvableApiException) {
            try {
                val intentSenderRequest = IntentSenderRequest
                    .Builder(exception.resolution)
                    .build()
                onDisabled(intentSenderRequest)
            } catch (sendEx: IntentSender.SendIntentException) {
                // ignore here
            }
        }
    }

}



