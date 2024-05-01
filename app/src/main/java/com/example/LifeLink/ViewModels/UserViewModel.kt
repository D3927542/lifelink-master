package com.example.LifeLink.ViewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.LifeLink.Models.User
import com.example.LifeLink.data.interfaces.IUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor( private val repository: IUserRepo) : ViewModel() {
    var currentuser by mutableStateOf(User(name = "", image = byteArrayOf()))
        private set

    init {
        currentuser = repository.getUser()
    }

    var currentUserFlow = repository.getUserFlow()

    var userExist = repository.checkUserExists()

    fun add(user: User) = viewModelScope.launch {
        repository.addUser(user)
    }

    fun edit(user: User) = viewModelScope.launch {
        repository.editUser(user)
        currentuser = repository.getUser()
    }

}