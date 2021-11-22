package hu.triszt4n.kittygram.api

import hu.triszt4n.kittygram.api.model.WebKitty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KittyApi {
    @GET("cat/{id}?json=true")
    suspend fun getKitty(@Path("id") id: String): Response<WebKitty>

    @GET("cat?json=true")
    suspend fun getRandomKitty(): Response<WebKitty>

    @GET("api/cats?tags={tag}&skip={skip}&limit={limit}")
    suspend fun getAllKitties(
        @Path("tag") tag: String = "",
        @Path("skip") skip: Int = 0,
        @Path("limit") limit: Int? = null
    ): Response<List<WebKitty>>

    @GET("api/tags")
    suspend fun getPossibleTags(): Response<List<String>>
}
