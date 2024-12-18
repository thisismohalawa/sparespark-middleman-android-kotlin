package sparespark.middleman.data.repository

import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.user.User


interface LoginRepository {
    suspend fun getAuthUser(): Result<Exception, User?>
    suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit>
    suspend fun signUpWithEmailAndPass(email: String, password: String): Result<Exception, Unit>
    suspend fun signInWithEmailAndPass(email: String, password: String): Result<Exception, Unit>
}