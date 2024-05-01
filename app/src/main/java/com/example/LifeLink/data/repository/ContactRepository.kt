package com.example.LifeLink.data.repository

import com.example.LifeLink.Models.Contact
import com.example.LifeLink.data.dao.IDao
import com.example.LifeLink.data.interfaces.IContactRepo
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val dao: IDao) : IContactRepo {
    override suspend fun addContact(contact: Contact)  = dao.addContact(contact)
    override suspend fun delete(contact: Contact) = dao.deleteContact(contact)

    override fun getAllContacts(): Flow<List<Contact>> = dao.getAllContact()
}