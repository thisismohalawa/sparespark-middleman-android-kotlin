package sparespark.middleman.data.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import sparespark.middleman.core.mapping.toUser
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.repository.LoginRepository
import sparespark.middleman.data.model.user.User
import sparespark.middleman.core.awaitTaskCompletable

class LoginRepositoryImpl(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : LoginRepository {

    override suspend fun getAuthUser(): Result<Exception, User?> = Result.build {
        auth.currentUser?.toUser
    }

    override suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit> = Result.build {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        awaitTaskCompletable(auth.signInWithCredential(credential))
    }

    override suspend fun signInWithEmailAndPass(
        email: String,
        password: String
    ): Result<Exception, Unit> = Result.build {
        awaitTaskCompletable(auth.signInWithEmailAndPassword(email, password))
    }

    override suspend fun signUpWithEmailAndPass(
        email: String,
        password: String
    ): Result<Exception, Unit> = Result.build {
        awaitTaskCompletable(auth.createUserWithEmailAndPassword(email, password))
    }
}