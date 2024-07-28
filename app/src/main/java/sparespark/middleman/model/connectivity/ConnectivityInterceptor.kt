package sparespark.middleman.model.connectivity

import okhttp3.Interceptor


interface ConnectivityInterceptor : Interceptor {
    fun isOnline(): Boolean
}
