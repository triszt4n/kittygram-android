package hu.triszt4n.kittygram.api

import hu.triszt4n.kittygram.api.model.KittyJson
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KittyApi {
    @GET("cat/{id}?json=true")
    suspend fun getKitty(@Path("id") id: String): Response<KittyJson>

    @GET("cat?json=true")
    suspend fun getRandomKitty(): Response<KittyJson>

    @GET("api/cats?skip={skip}&limit={limit}")
    suspend fun getAllKitties(
        @Path("skip") skip: Int = 0,
        @Path("limit") limit: Int
    ): Response<List<KittyJson>>

    @GET("api/cats?tags={tag}")
    suspend fun getAllKittiesWithTag(@Path("tag") tag: String): Response<List<KittyJson>>
}
