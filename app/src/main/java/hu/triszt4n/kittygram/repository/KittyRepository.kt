package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.api.RetrofitInstance
import hu.triszt4n.kittygram.api.model.KittyJson
import retrofit2.Response

class KittyRepository {

    suspend fun getKitty(id: String): Response<KittyJson> {
        return RetrofitInstance.api.getKitty(id)
    }

}
