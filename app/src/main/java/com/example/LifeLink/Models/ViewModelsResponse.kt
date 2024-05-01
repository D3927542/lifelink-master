package com.example.LifeLink.Models

import com.example.LifeLink.ViewModels.ContactViewModel
import com.example.LifeLink.ViewModels.LocationViewModel
import com.example.LifeLink.ViewModels.MedicalViewModel
import com.example.LifeLink.ViewModels.UserViewModel

data class ViewModelsResponse(
    val userVm : UserViewModel,
    val contactVm : ContactViewModel,
    val locationVm : LocationViewModel,
    val medicalVm : MedicalViewModel,
)
