package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.api.RetrofitInstance
import hu.triszt4n.kittygram.model.Kitty
import retrofit2.Response

class KittyRepository {

    suspend fun getKitty(id: String): Response<Kitty> {
        return RetrofitInstance.api.getKitty(id)
    }

}
