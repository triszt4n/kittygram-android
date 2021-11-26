package hu.triszt4n.kittygram.data.dao

import androidx.room.*
import hu.triszt4n.kittygram.data.entity.Collection
import hu.triszt4n.kittygram.data.entity.Kitty

@Dao
interface KittyDao {
    @Query("SELECT * FROM kitty ORDER BY insertedAt DESC")
    suspend fun findAll(): List<Kitty>

    @Query("SELECT * FROM kitty WHERE id = :id")
    suspend fun findById(id: Long): Kitty?

    @Query("SELECT * FROM kitty WHERE webId = :webId")
    suspend fun findByWebId(webId: String): Kitty?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(kitty: Kitty): Long?

    @Update
    suspend fun update(kitty: Kitty)

    @Delete
    suspend fun delete(kitty: Kitty)
}
