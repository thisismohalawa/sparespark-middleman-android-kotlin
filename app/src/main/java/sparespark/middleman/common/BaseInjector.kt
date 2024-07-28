package sparespark.middleman.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sparespark.middleman.model.connectivity.ConnectivityInterceptorImpl
import sparespark.middleman.model.implementation.BrandRepositoryImpl
import sparespark.middleman.model.implementation.CartRepositoryImpl
import sparespark.middleman.model.implementation.LoginRepositoryImpl
import sparespark.middleman.model.implementation.ProductRepositoryImpl
import sparespark.middleman.model.preference.BrandPreferenceImpl
import sparespark.middleman.model.preference.ProductPreferenceImpl
import sparespark.middleman.model.repository.LoginRepository
import sparespark.middleman.model.room.XDatabase

abstract class BaseInjector(application: Application) : AndroidViewModel(application) {

    protected fun connectInterceptorImpl() = ConnectivityInterceptorImpl(getApplication())

    private fun localProductDaoImpl() = XDatabase.getInstance(getApplication()).productDao()

    private fun localProductPrefImpl() = ProductPreferenceImpl(getApplication())

    private fun localUserDaoImpl() = XDatabase.getInstance(getApplication()).userDao()

    protected fun getLoginRepository(): LoginRepository =
        LoginRepositoryImpl(
            local = localUserDaoImpl(),
            connectInterceptor = connectInterceptorImpl()
        )

    protected fun getProductRepository() =
        ProductRepositoryImpl(
            productPref = localProductPrefImpl(),
            local = localProductDaoImpl(),
            connectInterceptor = connectInterceptorImpl()
        )

    protected fun getBrandRepository() =
        BrandRepositoryImpl(
            connectInterceptor = connectInterceptorImpl(),
            brandPref = BrandPreferenceImpl(getApplication()),
            local = XDatabase.getInstance(getApplication()).brandDao()
        )

    protected fun getCartRepository() =
        CartRepositoryImpl(
            local = XDatabase.getInstance(getApplication()).cartDao()
        )

}