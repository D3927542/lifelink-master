package com.example.LifeLink.data.interfaces

import com.example.LifeLink.Models.User
import kotlinx.coroutines.flow.Flow

interface IUserRepo {
    suspend fun addUser(user: User)
    suspend fun editUser(user: User)
    fun getUser(): User
    fun getUserFlow(): Flow<User>
    fun checkUserExists() : Boolean
}