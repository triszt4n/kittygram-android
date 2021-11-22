package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.CollectionWithKitties
import hu.triszt4n.kittygram.data.entity.Collection

@Dao
interface CollectionDao {
    @Query("SELECT * FROM collection")
    suspend fun getAll(): List<Collection>

    @Transaction
    @Query("SELECT * FROM collection WHERE id = :id")
    suspend fun getById(id: Long): CollectionWithKitties

    @Insert
    fun insert(collection: Collection)

    @Update
    fun update(collection: Collection)

    @Delete
    fun delete(collection: Collection)
}