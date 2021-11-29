package hu.triszt4n.kittygram.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hu.triszt4n.kittygram.data.util.Converter
import java.util.*

@Entity(tableName = "kitty", indices = [Index(value = ["webId"], unique = true)])
@TypeConverters(Converter::class)
data class Kitty(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    val collectionId: Long,

    val webId: String,
    val tags: List<String> = listOf(),
    val url: String,

    var rating: Int? = null,
    var name: String = webId,
    var insertedAt: Date = Date(),
)
