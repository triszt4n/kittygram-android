package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.api.RetrofitInstance
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Kitty
import retrofit2.Response

class KittyRepository(private val kittyDao: KittyDao) {

    suspend fun addKitty(kitty: Kitty) {
        kittyDao.insert(kitty)
    }

    suspend fun getKitty(id: String): Response<WebKitty> {
        return RetrofitInstance.api.getKitty(id)
    }

}
