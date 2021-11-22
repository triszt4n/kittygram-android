package hu.triszt4n.kittygram.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class Collection(
    @PrimaryKey(autoGenerate = true) var id: Long,
    var name: String
)
