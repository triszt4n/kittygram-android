package hu.triszt4n.kittygram.api

import hu.triszt4n.kittygram.api.model.WebKitty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KittyApi {
    @GET("cat/{id}?json=true")
    suspend fun getKitty(@Path("id") id: String): Response<WebKitty>

    @GET("cat?json=true")
    suspend fun getRandomKitty(): Response<WebKitty>

    @GET("api/cats")
    suspend fun getAllKitties(
        @Query("tags") tag: String = "",
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 1000
    ): Response<List<WebKitty>>

    @GET("api/tags")
    suspend fun getPossibleTags(): Response<List<String>>
}
