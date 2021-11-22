package hu.triszt4n.kittygram.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hu.triszt4n.kittygram.data.util.Converter
import hu.triszt4n.kittygram.api.model.WebKitty
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
    var insertedAt: Date = Date(),
) {
    constructor(json: WebKitty, collectionId: Long, rating: Int? = null): this(
        webId = json.id,
        tags = json.tags,
        url = json.url,

        rating = rating,
        collectionId = collectionId
    )
}
