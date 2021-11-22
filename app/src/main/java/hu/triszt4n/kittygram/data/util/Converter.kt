package hu.triszt4n.kittygram.data.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object Converter {
    private val gson: Gson = Gson()
    private val itemType: Type = object : TypeToken<List<String>>() {}.type

    @TypeConverter
    fun stringToStringList(value: String?): List<String> {
        return if (value == null)
            listOf()
        else
            gson.fromJson(value, itemType)
    }

    @TypeConverter
    fun stringListToString(list: List<String>): String {
        return gson.toJson(list)
    }
}
