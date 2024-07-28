package sparespark.middleman.model.repository

import sparespark.middleman.common.Result
import sparespark.middleman.model.User


interface LoginRepository {
    suspend fun getAuthUser(): Result<Exception, User?>
    suspend fun signOutCurrentUser(): Result<Exception, Unit>
    suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit>
    suspend fun isUserExist(): Result<Exception, Boolean?>
    suspend fun updateLocalUser(user: User): Result<Exception, Boolean>
    suspend fun deleteLocalUser(): Result<Exception, Unit>
}