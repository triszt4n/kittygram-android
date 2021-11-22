package hu.triszt4n.kittygram.data

import androidx.room.*

@Dao
interface KittyDao {
    @Query("SELECT * FROM kitty")
    suspend fun getAll(): List<KittyEntity>

    @Query("SELECT * FROM kitty")
    suspend fun getAllFromCollection(collection: CollectionEntity): List<KittyEntity>

    @Query("SELECT * FROM kitty WHERE id = :id")
    suspend fun getById(id: Int): List<KittyEntity>

    @Insert
    fun insert(kitty: KittyEntity)

    @Update
    fun update(kitty: KittyEntity)

    @Delete
    fun delete(kitty: KittyEntity)
}
