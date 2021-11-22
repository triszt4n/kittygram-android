package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

@Dao
interface KittyDao {
    @Query("SELECT * FROM kitty")
    suspend fun getAll(): List<Kitty>

    @Query("SELECT * FROM kitty")
    suspend fun getAllFromCollection(collection: Collection): List<Kitty>

    @Query("SELECT * FROM kitty WHERE id = :id")
    suspend fun getById(id: Long): List<Kitty>

    @Insert
    fun insert(kitty: Kitty)

    @Update
    fun update(kitty: Kitty)

    @Delete
    fun delete(kitty: Kitty)
}
