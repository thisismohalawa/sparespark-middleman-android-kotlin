package sparespark.middleman.data.connectivity

import okhttp3.Interceptor


interface ConnectivityInterceptor : Interceptor {
    fun isOnline(): Boolean
}
