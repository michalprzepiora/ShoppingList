package pl.com.przepiora.shoppinglist.service

import pl.com.przepiora.shoppinglist.model.Entry

class EntryRepositoryNetwork(private val entryRepository: EntryRepository) {

    suspend fun add(entry: Entry){
         entryRepository.add(entry)
    }
}