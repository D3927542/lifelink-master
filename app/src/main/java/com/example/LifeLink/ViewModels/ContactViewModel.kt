package com.example.LifeLink.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.LifeLink.Models.Contact
import com.example.LifeLink.data.interfaces.IContactRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: IContactRepo) : ViewModel() {
    var contacts = repository.getAllContacts()

    fun add(contact: Contact) = viewModelScope.launch {
        repository.addContact(contact)
        contacts = repository.getAllContacts()
    }

    fun delete(contact: Contact) = viewModelScope.launch {
        repository.delete(contact)
        contacts = repository.getAllContacts()
    }
}