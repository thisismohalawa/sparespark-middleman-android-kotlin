package sparespark.middleman.model.implementation

import sparespark.middleman.model.connectivity.ConnectivityInterceptor

abstract class BaseRepository(
    private val connectInterceptor: ConnectivityInterceptor,
) {
    protected fun hasInternetConnection(): Boolean = connectInterceptor.isOnline()

}