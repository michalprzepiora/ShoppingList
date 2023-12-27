package pl.com.przepiora.shoppinglist.service

import pl.com.przepiora.shoppinglist.model.Entry

class EntryRepositoryInMemory : EntryRepository {
    var entryList = mutableListOf<Entry>()
    override fun getAll(): List<Entry> {
        return entryList
    }

    override fun removeEntry(entry: Entry) {
        entryList.remove(entry)
    }

    override fun addEntry(entry: Entry) {
        entryList.add(entry)
    }

    override fun update(entryList: List<Entry>) {
        this.entryList = entryList.toMutableList();
    }
}