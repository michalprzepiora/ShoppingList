package pl.com.przepiora.shoppinglist.service.configuration

import pl.com.przepiora.shoppinglist.service.EntryRepository
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitConfiguration {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://shopping-list-7fc9e-default-rtdb.europe-west1.firebasedatabase.app/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val entryRepository: EntryRepository = retrofit.create(EntryRepository::class.java)
}