package sparespark.middleman.model.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sparespark.middleman.model.IColor
import sparespark.middleman.model.IRelated
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromColors(list: ArrayList<IColor>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<IColor?>?>() {}.type
        return gson.toJson(list, type)
    }


    @TypeConverter
    fun toColors(string: String?): ArrayList<IColor>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<IColor?>?>() {}.type
        return gson.fromJson<ArrayList<IColor>>(string, type)
    }

    @TypeConverter
    fun fromRelated(list: ArrayList<IRelated>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<IRelated?>?>() {}.type
        return gson.toJson(list, type)
    }


    @TypeConverter
    fun toRelated(string: String?): ArrayList<IRelated>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<IRelated?>?>() {}.type
        return gson.fromJson<ArrayList<IRelated>>(string, type)
    }
}
