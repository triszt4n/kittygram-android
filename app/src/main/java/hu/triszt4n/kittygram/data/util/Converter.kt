package hu.triszt4n.kittygram.data.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import hu.triszt4n.kittygram.util.MoshiInstance
import java.lang.reflect.Type
import java.util.*

object Converter {
    private val jsonAdapter: JsonAdapter<List<String>> by lazy {
        val stringListType: Type = Types.newParameterizedType(List::class.java, String::class.java)
        MoshiInstance.moshi.adapter(stringListType)
    }

    @TypeConverter
    fun fromString(value: String): List<String> = jsonAdapter.fromJson(value).orEmpty()

    @TypeConverter
    fun tagsToString(list: List<String>): String = jsonAdapter.toJson(list)

    @TypeConverter
    fun fromTimestamp(value: Long): Date = Date(value)

    @TypeConverter
    fun dateToTimestamp(date: Date): Long = date.time
}
