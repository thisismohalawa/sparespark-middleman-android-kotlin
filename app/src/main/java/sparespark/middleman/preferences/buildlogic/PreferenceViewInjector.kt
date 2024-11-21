package sparespark.middleman.preferences.buildlogic

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sparespark.middleman.core.base.BaseInjector
import sparespark.middleman.data.implementation.PreferenceRepositoryImpl
import sparespark.middleman.data.preferences.cache.CacheBasePreferenceImpl
import sparespark.middleman.data.room.MiddlemanDatabase

class PreferenceViewInjector(
    app: Application
) : AndroidViewModel(app) {

    private fun getUtilCachePref() = CacheBasePreferenceImpl(getApplication())

    private fun getPreferenceRepository() = PreferenceRepositoryImpl(
        localDB = MiddlemanDatabase.getInstance(getApplication()),
        cachePreference = getUtilCachePref()
    )

    fun provideViewModelFactory() =
        PreferenceViewModelFactory(getPreferenceRepository())
}
