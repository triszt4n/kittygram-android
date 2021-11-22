package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.entity.Collection

@Dao
interface CollectionDao {
    @Transaction
    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<CollectionWithKitties>

    @Transaction
    @Query("SELECT * FROM collection WHERE id = :id")
    suspend fun getById(id: Long?): CollectionWithKitties

    @Insert
    suspend fun insert(collection: Collection)

    @Update
    suspend fun update(collection: Collection)

    @Delete
    suspend fun delete(collection: Collection)
}