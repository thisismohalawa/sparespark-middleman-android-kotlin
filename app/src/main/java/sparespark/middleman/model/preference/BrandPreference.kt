package sparespark.middleman.model.preference

interface BrandPreference {
    fun updateListCacheTimeToNow()
    fun clearListCacheTime(): Boolean
    fun isListUpdateNeeded(): Boolean
}