package hu.triszt4n.kittygram.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection")
data class Collection(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) var id: Long,
    @ColumnInfo(name = "name") var name: String
)
