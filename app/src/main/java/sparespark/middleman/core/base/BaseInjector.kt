package sparespark.middleman.core.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import sparespark.middleman.data.connectivity.ConnectivityInterceptorImpl
import sparespark.middleman.data.implementation.CartRepositoryImpl
import sparespark.middleman.data.implementation.UserRepositoryImpl
import sparespark.middleman.data.repository.UserRepository
import sparespark.middleman.data.room.MiddlemanDatabase

abstract class BaseInjector(application: Application) : AndroidViewModel(application) {

    protected fun connectInterceptorImpl() = ConnectivityInterceptorImpl(getApplication())

    protected fun localUserDaoImpl() = MiddlemanDatabase.getInstance(getApplication()).userDao()

    protected fun localCartDao() = MiddlemanDatabase.getInstance(getApplication()).cartDao()

    protected fun getUserRepository(): UserRepository = UserRepositoryImpl(
        local = localUserDaoImpl(),
        connectInterceptor = connectInterceptorImpl()
    )

    protected fun getCartRepository() = CartRepositoryImpl(
        local = localCartDao()
    )
}