package sparespark.middleman.model.preference

import android.content.Context
import org.threeten.bp.ZonedDateTime

private const val BRAND_LAST_CACHE = "BRAND_LAST_CACHE"
private const val BRANDS_CACHE_TIME = "BRAND_CACHE_TIME"

class BrandPreferenceImpl(
    context: Context,
) : PreferenceProvider(context), BrandPreference {

    private val inputCacheTime = try {
        preferences.getString(BRANDS_CACHE_TIME, "48")?.toInt() ?: 48
    } catch (ex: Exception) {
        48
    }

    private fun getListLastCacheTime(): String? =
        preferences.getString(BRAND_LAST_CACHE, null)

    override fun updateListCacheTimeToNow() =
        preferencesEditor.putString(BRAND_LAST_CACHE, ZonedDateTime.now().toString()).apply()


    override fun clearListCacheTime(): Boolean =
        preferencesEditor.remove(BRAND_LAST_CACHE).commit()

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
