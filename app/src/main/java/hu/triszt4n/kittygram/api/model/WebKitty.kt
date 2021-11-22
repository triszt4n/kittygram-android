package hu.triszt4n.kittygram.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
data class WebKitty(
    val id: String,
    val tags: List<String>,
    @Json(name = "created_at") val createdAt: Date, /// unused property
    val url: String = "cat/$id"
)
