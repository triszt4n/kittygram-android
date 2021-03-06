package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.api.RetrofitInstance
import hu.triszt4n.kittygram.api.model.WebKitty
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Kitty
import retrofit2.Response

class KittyRepository(
    private val kittyDao: KittyDao
) {
    companion object {
        const val PAGING_LIMIT = 10
    }

    suspend fun getPaginatedWebKitties(
        tag: String?,
        page: Int = 0
    ): Response<MutableList<WebKitty>> {
        return RetrofitInstance.api.getAllKitties(
            tag = tag.orEmpty(),
            limit = PAGING_LIMIT,
            skip = (page - 1) * PAGING_LIMIT
        )
    }

    suspend fun addKitty(kitty: Kitty): Kitty? {
        val foundKitty = kittyDao.findByWebId(kitty.webId)
        if (foundKitty == null) {
            val id = kittyDao.insert(kitty)
            kitty.id = id
            return kitty
        }
        return null
    }

    suspend fun deleteKitty(kitty: Kitty) {
        kittyDao.delete(kitty)
    }

    suspend fun updateKitty(kitty: Kitty) {
        kittyDao.update(kitty)
    }
}
