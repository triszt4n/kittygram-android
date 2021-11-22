package hu.triszt4n.kittygram.api

import hu.triszt4n.kittygram.model.Kitty
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface KittyApi {
    @GET("cat/{id}?json=true")
    suspend fun getKitty(@Path("id") id: String): Response<Kitty>

    @GET("cat?json=true")
    suspend fun getRandomKitty(): Kitty

    @GET("api/cats?tags={tag}")
    suspend fun getAllWithTag(@Path("tag") tag: String): List<Kitty>
}
