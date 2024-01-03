package pl.com.przepiora.shoppinglist.service

import pl.com.przepiora.shoppinglist.model.Entry
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface EntryRepository {
    @POST("/entries.json")
    suspend fun add(@Body entry: Entry)

    @GET("/entries.json")
    suspend fun getAll(): Map<String, Entry>

    @DELETE("/entries/{id}.json")
    suspend fun delete(@Path("id")id: String)

}