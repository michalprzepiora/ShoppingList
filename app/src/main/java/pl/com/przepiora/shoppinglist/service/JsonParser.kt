package pl.com.przepiora.shoppinglist.service

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import pl.com.przepiora.shoppinglist.model.Entry

class JsonParser {
    private val moshi = Moshi.Builder().build()

    fun entryToJson(entry: Entry): String{
        val type = Entry::class.java
        return moshi.adapter<Entry>(type).toJson(entry)
    }

    fun entryToJson(list: List<Entry>): String{
        val type = Types.newParameterizedType(List::class.java, Entry::class.java)
        return moshi.adapter<List<Entry>>(type).toJson(list)
    }

    fun jsonToEntryList(json: String): List<Entry>{
        val type = Types.newParameterizedType(List::class.java, Entry::class.java)
        return moshi.adapter<List<Entry>>(type).fromJson(json) ?: emptyList()
    }
}