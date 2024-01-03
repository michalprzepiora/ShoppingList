package pl.com.przepiora.shoppinglist.service

import android.util.Log
import pl.com.przepiora.shoppinglist.model.Entry

class EntryRepositoryNetwork(private val entryRepository: EntryRepository) {

    suspend fun add(entry: Entry){
         entryRepository.add(entry)
    }

    suspend fun getAll(): List<Entry>{
        Log.d("Network", "Call getAll() to firebase")
        return entryRepository.getAll().values.toMutableList()
    }
}