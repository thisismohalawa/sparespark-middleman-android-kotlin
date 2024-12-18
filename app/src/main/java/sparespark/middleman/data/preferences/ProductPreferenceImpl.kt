package sparespark.middleman.data.preferences

import android.content.Context
import org.threeten.bp.ZonedDateTime
import sparespark.middleman.core.PRODUCT_CACHE_TIME
import sparespark.middleman.data.preferences.base.BaseListPreference
import sparespark.middleman.data.preferences.base.BasePreferenceCacheProvider

class ProductPreferenceImpl(
    context: Context,
) : BasePreferenceCacheProvider(context), BaseListPreference {

    private fun inputProductCTime() = sharedPref.getString(PRODUCT_CACHE_TIME, "4")?.toInt() ?: 4

    private fun getLastCacheTime() = sharedPref.getString(productLastCache, null)

    override fun updateCacheTimeToNow() =
        prefEditor.putString(productLastCache, ZonedDateTime.now().toString()).apply()

    override fun clearListCacheTime() = prefEditor.remove(productLastCache).commit()

    override fun isListUpdateNeeded(): Boolean {
        if (getLastCacheTime() == null) return true
        return try {
            val timeAgo = ZonedDateTime.now().minusHours(inputProductCTime().toLong())
            val fetchedTime = ZonedDateTime.parse(getLastCacheTime())
            fetchedTime.isBefore(timeAgo)
        } catch (ex: Exception) {
            true
        }
    }

    override fun isZeroInputCacheTime() = inputProductCTime() == 0
}