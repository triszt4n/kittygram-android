package hu.triszt4n.kittygram.data.util

import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types
import hu.triszt4n.kittygram.util.MoshiInstance
import java.lang.reflect.Type

object Converter {
    private val jsonAdapter: JsonAdapter<List<String>> by lazy {
        val stringListType: Type = Types.newParameterizedType(List::class.java, String::class.java)
        MoshiInstance.moshi.adapter(stringListType)
    }

    @TypeConverter
    fun stringToTags(value: String): List<String> {
        return jsonAdapter.fromJson(value).orEmpty()
    }

    @TypeConverter
    fun tagsToString(list: List<String>): String {
        return jsonAdapter.toJson(list)
    }
}
