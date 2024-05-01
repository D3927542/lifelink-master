package com.example.LifeLink.data.interfaces

import com.example.LifeLink.Models.Contact
import kotlinx.coroutines.flow.Flow

interface IContactRepo {
    suspend fun addContact(contact: Contact)
    suspend fun delete(contact: Contact)
    fun getAllContacts(): Flow<List<Contact>>
}