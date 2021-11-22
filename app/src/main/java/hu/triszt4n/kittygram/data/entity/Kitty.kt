package hu.triszt4n.kittygram.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import hu.triszt4n.kittygram.data.util.Converter
import hu.triszt4n.kittygram.api.model.KittyJson
import java.util.*

@Entity(tableName = "kitty")
@TypeConverters(Converter::class)
data class Kitty(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var webId: String,
    var createdAt: Date,
    var tags: List<String> = listOf(),
    var url: String,
    var rating: Int? = null
) {
    constructor(json: KittyJson): this(
        webId = json.id,
        createdAt = json.createdAt,
        tags = json.tags,
        url = json.url
    )

    constructor(json: KittyJson, rating: Int): this(
        json = json
    ) {
        this.rating = rating
    }
}
