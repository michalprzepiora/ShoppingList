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

    suspend fun delete(entry: Entry){
        val id = getId(entry)
        Log.d("Network delete", "Id: $id")
        if (id != null){
            entryRepository.delete(id)
        }
    }

    suspend fun changeDone(entry: Entry){
        val id = getId(entry)
        if (id != null){
            entry.isDone = !entry.isDone
            entryRepository.update(id, entry)
        }
    }

    private suspend fun getId(entry: Entry): String?{
        return entryRepository.getAll().filterValues { it.text == entry.text }.firstNotNullOfOrNull { entrySet -> entrySet.key }
    }
}