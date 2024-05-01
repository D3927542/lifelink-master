package com.example.LifeLink.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.Models.Medical
import com.example.LifeLink.Models.User
import kotlinx.coroutines.flow.Flow
@Dao
interface IDao {
    @Query("SELECT * FROM User")
     fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM Medical")
     fun getAllMedical(): Flow<Medical>

    @Query("SELECT * FROM Contact")
     fun getAllContact(): Flow<List<Contact>>

    @Insert
     fun addContact(contact: Contact)

    @Delete
     fun deleteContact(contact: Contact)

    @Insert
     fun addUser(user: User)

    @Insert
     fun addMedical(medical: Medical)

     @Update
     fun editMedical(medical: Medical)
     @Update
     fun editUser(user: User)

     @Query("SELECT COUNT(id) FROM User")
     fun checkIfUserExist() : Int

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): User

    @Query("SELECT * FROM User LIMIT 1")
    fun getUserFlow(): Flow<User>
}