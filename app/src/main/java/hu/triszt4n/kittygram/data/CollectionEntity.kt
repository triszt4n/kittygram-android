package hu.triszt4n.kittygram.data

import androidx.room.Entity

@Entity(tableName = "collection")
data class CollectionEntity(
    val id: Int
)
