package sparespark.middleman.data.implementation.base

import sparespark.middleman.core.NoConnectivityException
import sparespark.middleman.core.UnAuthorizedException
import sparespark.middleman.data.connectivity.ConnectivityInterceptor
import sparespark.middleman.data.room.user.UserDao

open class BaseLocalUserPreferenceRepository(
    userDao: UserDao,
    private val connectInterceptor: ConnectivityInterceptor
) : BaseLocalUserRepository(userDao) {

    protected suspend fun checkUserAuthorizedCredentials() {
        if (currentUserId().isNullOrBlank() ||
            isUserExist() == false
        ) throw UnAuthorizedException()
    }

    protected fun checkNetConnection() {
        if (!connectInterceptor.isOnline()) throw NoConnectivityException()
    }
}