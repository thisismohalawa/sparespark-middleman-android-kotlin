package sparespark.middleman.data.preferences

import android.content.Context
import org.threeten.bp.ZonedDateTime
import sparespark.middleman.core.BRAND_CACHE_TIME
import sparespark.middleman.data.preferences.base.BaseListPreference
import sparespark.middleman.data.preferences.base.BasePreferenceCacheProvider

class BrandPreferenceImpl(
    context: Context,
) : BasePreferenceCacheProvider(context), BaseListPreference {

    private fun inputBrandCTime() = sharedPref.getString(BRAND_CACHE_TIME, "4")?.toInt() ?: 4

    private fun getLastCacheTime() = sharedPref.getString(brandLastCache, null)

    override fun updateCacheTimeToNow() =
        prefEditor.putString(brandLastCache, ZonedDateTime.now().toString()).apply()

    override fun clearListCacheTime() = prefEditor.remove(brandLastCache).commit()

    override fun isListUpdateNeeded(): Boolean {
        if (getLastCacheTime() == null) return true
        return try {
            val timeAgo = ZonedDateTime.now().minusHours(inputBrandCTime().toLong())
            val fetchedTime = ZonedDateTime.parse(getLastCacheTime())
            fetchedTime.isBefore(timeAgo)
        } catch (ex: Exception) {
            true
        }
    }

    override fun isZeroInputCacheTime() = inputBrandCTime() == 0
}