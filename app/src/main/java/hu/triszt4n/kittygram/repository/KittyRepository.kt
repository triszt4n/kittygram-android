package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.api.RetrofitInstance
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Kitty
import retrofit2.Response

class KittyRepository(
    private val kittyDao: KittyDao)
{
    suspend fun getAllWebKitties(tag: String?): Response<List<WebKitty>> {
        return RetrofitInstance.api.getAllKitties(tag.orEmpty())
    }

    suspend fun addKitty(kitty: Kitty): Boolean {
        val foundKitty = kittyDao.findByWebId(kitty.webId)
        return if (foundKitty == null) {
            kittyDao.insert(kitty)
            true
        } else {
            false
        }
    }

    suspend fun deleteKitty(kitty: Kitty) {
        kittyDao.delete(kitty)
    }

    suspend fun updateKitty(kitty: Kitty) {
        kittyDao.update(kitty)
    }
}
