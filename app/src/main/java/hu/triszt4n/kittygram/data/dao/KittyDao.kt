package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

@Dao
interface KittyDao {
    @Query("SELECT * FROM kitty ORDER BY insertedAt DESC")
    suspend fun getAll(): List<Kitty>

    @Query("SELECT * FROM kitty WHERE id = :id")
    suspend fun getById(id: Long): Kitty

    @Query("SELECT * FROM kitty WHERE webId = :webId")
    suspend fun getByWebId(webId: Long): Kitty

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kitty: Kitty)

    @Update
    fun update(kitty: Kitty)

    @Delete
    fun delete(kitty: Kitty)
}
