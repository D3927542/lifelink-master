package com.example.LifeLink.Service

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation():Location?
}