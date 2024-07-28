package sparespark.middleman.model.preference

import android.content.Context
import org.threeten.bp.ZonedDateTime

private const val PRODUCT_LAST_CACHE = "PRODUCT_LAST_CACHE"
private const val PRODUCT_CACHE_TIME = "PRODUCT_CACHE_TIME"

class ProductPreferenceImpl(
    context: Context,
) : PreferenceProvider(context), ProductPreference {

    private val inputCacheTime = try {
        preferences.getString(PRODUCT_CACHE_TIME, "6")?.toInt() ?: 6
    } catch (ex: Exception) {
        6
    }

    private fun getListLastCacheTime(): String? =
        preferences.getString(PRODUCT_LAST_CACHE, null)

    override fun updateListCacheTimeToNow() =
        preferencesEditor.putString(
            PRODUCT_LAST_CACHE,
            ZonedDateTime.now().toString()
        ).apply()


    override fun clearListCacheTime(): Boolean =
        preferencesEditor.remove(
            PRODUCT_LAST_CACHE
        ).commit()

    override fun isListUpdateNeeded(): Boolean {
        if (getListLastCacheTime() == null) return true
        return try {
            val timeAgo = ZonedDateTime.now().minusHours(inputCacheTime.toLong())
            val fetchedTime = ZonedDateTime.parse(getListLastCacheTime())
            fetchedTime.isBefore(timeAgo)
        } catch (ex: Exception) {
            true
        }
    }
}
