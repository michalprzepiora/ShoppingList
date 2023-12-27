package pl.com.przepiora.shoppinglist.service

import pl.com.przepiora.shoppinglist.model.Entry

interface EntryRepository {
    fun getAll(): List<Entry>
    fun removeEntry(entry: Entry)
    fun addEntry(entry: Entry)
    fun update(entryList: List<Entry>)
}