package com.example.LifeLink.data.interfaces

import com.example.LifeLink.Models.Medical
import kotlinx.coroutines.flow.Flow

interface IMedicalRepo {
    suspend fun addMedcial(medical: Medical)
    suspend fun editMedcial(medical: Medical)
    fun getMedical(): Flow<Medical>
}