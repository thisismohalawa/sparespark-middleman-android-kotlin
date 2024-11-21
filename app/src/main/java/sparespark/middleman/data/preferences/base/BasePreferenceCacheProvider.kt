package sparespark.middleman.data.preferences.base

import android.content.Context

open class BasePreferenceCacheProvider(context: Context) :
    BasePreferenceProvider(context) {

    protected val productLastCache = "product_last_cache"
    protected val brandLastCache = "brand_last_cache"

    protected fun actionClearLastCacheTimes() {
        if (sharedPref.getString(productLastCache, null) != null) {
            prefEditor.let {
                it.putString(productLastCache, null)
                it.putString(brandLastCache, null)
                it.apply()
            }
        }
    }
}