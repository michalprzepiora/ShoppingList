package pl.com.przepiora.shoppinglist.service

import pl.com.przepiora.shoppinglist.model.Entry
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface EntryRepository {
    @POST("/entries.json")
    suspend fun add(@Body entry: Entry)

    @GET("/entries.json")
    suspend fun getAll(): Map<String, Entry>

}