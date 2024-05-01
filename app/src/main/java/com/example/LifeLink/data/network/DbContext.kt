package com.example.LifeLink.data.network

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.LifeLink.data.dao.IDao
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.Models.Medical
import com.example.LifeLink.Models.User

@Database(entities = [User::class, Medical::class, Contact::class], version = 1)
abstract class DbContext: RoomDatabase() {
    abstract val dao: IDao
}