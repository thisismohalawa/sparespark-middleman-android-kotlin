package sparespark.middleman.data.preferences.base

interface BaseListPreference {

    fun updateCacheTimeToNow()

    fun clearListCacheTime(): Boolean

    fun isListUpdateNeeded(): Boolean

    fun isZeroInputCacheTime(): Boolean
}
