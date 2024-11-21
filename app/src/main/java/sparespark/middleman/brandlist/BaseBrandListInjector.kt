package sparespark.middleman.brandlist

import android.app.Application
import sparespark.middleman.core.base.BaseInjector
import sparespark.middleman.data.implementation.BrandRepositoryImpl
import sparespark.middleman.data.preferences.BrandPreferenceImpl
import sparespark.middleman.data.room.MiddlemanDatabase

open class BaseBrandListInjector(
    app: Application
) : BaseInjector(app) {

    private fun localBrandDaoImpl() = MiddlemanDatabase.getInstance(getApplication()).brandDao()

    private fun localBrandPrefImpl() = BrandPreferenceImpl(getApplication())

    protected fun getBrandRepository() = BrandRepositoryImpl(
        local = localBrandDaoImpl(),
        pref = localBrandPrefImpl(),
        localUser = localUserDaoImpl(),
        connectInterceptor = connectInterceptorImpl()
    )
}
