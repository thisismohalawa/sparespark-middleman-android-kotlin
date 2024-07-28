package sparespark.middleman.model.preference

interface ProductPreference {
    fun updateListCacheTimeToNow()
    fun clearListCacheTime(): Boolean
    fun isListUpdateNeeded(): Boolean
}
