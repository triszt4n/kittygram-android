package hu.triszt4n.kittygram.repository

import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.dao.CollectionDao
import hu.triszt4n.kittygram.data.dao.KittyDao
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.repository.exception.EntityNotFoundException

class CollectionRepository(
    private val collectionDao: CollectionDao,
    private val kittyDao: KittyDao
) {
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
        return collectionDao.findAll()
    }

    suspend fun getCollectionWithKitties(collection: Collection): CollectionWithKitties {
        return getCollectionWithKitties(collection.id)
    }

    suspend fun getCollectionWithKitties(collectionId: Long?): CollectionWithKitties {
        return collectionDao.findById(collectionId)?.apply {
            kitties = kitties.sortedByDescending {
                it.rating
            }
        }
            ?: throw EntityNotFoundException(
                "CollectionWithKitties with entered Id not found! " +
                        "Make it sure, the entity exists before calling a getXXX function!"
            )
    }
}