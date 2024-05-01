package com.example.LifeLink.Models

data class MedicalInfo(
    val name: String,
    val value: String,
    val image: Int,
    val userImage : ByteArray? = null
)
