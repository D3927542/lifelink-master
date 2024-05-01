package com.example.LifeLink.data.repository

import com.example.LifeLink.Models.Medical
import com.example.LifeLink.data.dao.IDao
import com.example.LifeLink.data.interfaces.IMedicalRepo
import kotlinx.coroutines.flow.Flow

class MedicalRepository(private val dao: IDao) : IMedicalRepo {
    override suspend fun addMedcial(medical: Medical) = dao.addMedical(medical)
    override suspend fun editMedcial(medical: Medical) = dao.editMedical(medical)

    override fun getMedical(): Flow<Medical> = dao.getAllMedical()
}