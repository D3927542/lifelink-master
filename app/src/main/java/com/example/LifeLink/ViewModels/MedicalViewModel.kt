package com.example.LifeLink.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.LifeLink.Models.Medical
import com.example.LifeLink.data.interfaces.IMedicalRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicalViewModel @Inject constructor(private val repository: IMedicalRepo) : ViewModel() {
    var medical = repository.getMedical()
    fun add(medical: Medical) = viewModelScope.launch {
        repository.addMedcial(medical)
    }

    fun edit(med: Medical) = viewModelScope.launch {
        repository.editMedcial(med)
        medical = repository.getMedical()
    }

}