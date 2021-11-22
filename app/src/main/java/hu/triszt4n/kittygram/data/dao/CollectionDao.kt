package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.entity.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<Collection>

    @Query("SELECT * FROM collection")
    suspend fun getAllWithKitties(collection: Collection): List<CollectionWithKitties>

    @Query("SELECT * FROM collection WHERE id = :id")
    suspend fun getById(id: Long): List<Collection>

    @Query("SELECT * FROM collection WHERE id = :id")
    suspend fun getWithKittiesById(id: Long): List<CollectionWithKitties>

    @Insert
    fun insert(collection: Collection)

    @Update
    fun update(collection: Collection)

    @Delete
    fun delete(collection: Collection)
}