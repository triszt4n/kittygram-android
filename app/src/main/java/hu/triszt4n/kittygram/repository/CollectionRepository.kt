package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.dao.CollectionDao
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Collection

class CollectionRepository(
    private val collectionDao: CollectionDao,
    private val kittyDao: KittyDao)
{
    suspend fun addCollection(collection: Collection) {
        collectionDao.insert(collection)
    }

    suspend fun updateCollection(collectionWithKitties: CollectionWithKitties) {
        collectionDao.update(collectionWithKitties.collection)
    }

    suspend fun deleteCollection(collectionWithKitties: CollectionWithKitties) {
        collectionWithKitties.kitties.forEach { kitty ->
            kittyDao.delete(kitty)
        }
        collectionDao.delete(collectionWithKitties.collection)
    }

    suspend fun getAllCollections(): List<CollectionWithKitties> {
        return collectionDao.getAll()
    }

    suspend fun getCollectionWithKitties(collection: Collection): CollectionWithKitties {
        return collectionDao.getById(collection.id)
    }
}