package sparespark.middleman.data.preferences.cache

import android.content.Context
import sparespark.middleman.data.preferences.base.BasePreferenceCacheProvider
import sparespark.middleman.data.preferences.base.BasePreferenceProvider

class CacheBasePreferenceImpl(
    context: Context
) : BasePreferenceCacheProvider(context), CachePreference {

    override fun clearLastCacheTimes() = actionClearLastCacheTimes()
}