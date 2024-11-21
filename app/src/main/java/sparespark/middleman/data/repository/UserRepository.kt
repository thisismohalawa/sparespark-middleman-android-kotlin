package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.user.User

interface UserRepository {
    suspend fun getLocalUser(): Result<Exception, User?>
    suspend fun getRemoteUser(): Result<Exception, User?>
    suspend fun updateLocalUser(user: User): Result<Exception, Unit>
    suspend fun pushUserToRemoteServer(user: User): Result<Exception, Unit>
    suspend fun signOutCurrentUser(): Result<Exception, Unit>
    suspend fun checkIfPushRequired(): Result<Exception, Boolean>
}