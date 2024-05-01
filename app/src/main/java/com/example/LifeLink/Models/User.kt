package com.example.LifeLink.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val image: ByteArray
)