package hu.triszt4n.kittygram.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "kitty")
@TypeConverters(Converter::class)
data class KittyEntity(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "webId") val webId: String,
    @ColumnInfo(name = "createdAt") val createdAt: Date,
    @ColumnInfo(name = "tags") val tags: List<String> = listOf(),
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "rating") val rating: Int? = null,
)
