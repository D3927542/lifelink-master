package com.example.LifeLink.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Medical (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var bloodType: String,
    var allergies: String,
    var medications: String,
    var medicalNotes: String,
    var existingCondition: String
)