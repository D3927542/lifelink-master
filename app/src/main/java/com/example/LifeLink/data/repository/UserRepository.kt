package com.example.LifeLink.data.repository

import com.example.LifeLink.Models.User
import com.example.LifeLink.data.dao.IDao
import com.example.LifeLink.data.interfaces.IUserRepo
import kotlinx.coroutines.flow.Flow

class UserRepository(private val dao: IDao) : IUserRepo {
    override suspend fun addUser(user: User) = dao.addUser(user)
    override suspend fun editUser(user: User) = dao.editUser(user)

    override fun getUser(): User = dao.getUser()
    override fun getUserFlow(): Flow<User> = dao.getUserFlow()

    override fun checkUserExists(): Boolean = dao.checkIfUserExist() >= 1
}