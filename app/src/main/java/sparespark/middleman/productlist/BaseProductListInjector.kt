package sparespark.middleman.productlist

import android.app.Application
import sparespark.middleman.core.base.BaseInjector
import sparespark.middleman.data.implementation.ProductRepositoryImpl
import sparespark.middleman.data.preferences.ProductPreferenceImpl
import sparespark.middleman.data.room.MiddlemanDatabase

open class BaseProductListInjector(
    app: Application
) : BaseInjector(app) {

    private fun localProductDaoImpl() = MiddlemanDatabase.getInstance(getApplication()).productDao()

    private fun localProductPrefImpl() = ProductPreferenceImpl(getApplication())

    protected fun getProductRepository() = ProductRepositoryImpl(
        local = localProductDaoImpl(),
        pref = localProductPrefImpl(),
        localUser = localUserDaoImpl(),
        connectInterceptor = connectInterceptorImpl()
    )
}
