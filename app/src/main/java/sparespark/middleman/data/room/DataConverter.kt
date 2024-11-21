package sparespark.middleman.data.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import sparespark.middleman.data.model.product.RelatedColor
import sparespark.middleman.data.model.product.RelatedUrl
import java.lang.reflect.Type

class DataConverter {
    @TypeConverter
    fun fromColors(list: ArrayList<RelatedColor>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<RelatedColor?>?>() {}.type
        return gson.toJson(list, type)
    }


    @TypeConverter
    fun toColors(string: String?): ArrayList<RelatedColor>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<RelatedColor?>?>() {}.type
        return gson.fromJson<ArrayList<RelatedColor>>(string, type)
    }

    @TypeConverter
    fun fromRelated(list: ArrayList<RelatedUrl>?): String? {
        if (list == null) {
            return null
        }
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<RelatedUrl?>?>() {}.type
        return gson.toJson(list, type)
    }


    @TypeConverter
    fun toRelated(string: String?): ArrayList<RelatedUrl>? {
        if (string == null) {
            return null
        }
        val gson = Gson()
        val type = object : TypeToken<ArrayList<RelatedUrl?>?>() {}.type
        return gson.fromJson<ArrayList<RelatedUrl>>(string, type)
    }
}
