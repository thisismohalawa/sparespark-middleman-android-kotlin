package sparespark.middleman.model.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import sparespark.middleman.common.Result
import sparespark.middleman.common.awaitTaskCompletable
import sparespark.middleman.common.lazyDeferred
import sparespark.middleman.common.toRoomUser
import sparespark.middleman.common.toUser
import sparespark.middleman.model.User
import sparespark.middleman.model.connectivity.ConnectivityInterceptor
import sparespark.middleman.model.repository.LoginRepository
import sparespark.middleman.model.room.UserDao

class LoginRepositoryImpl(
    private val local: UserDao,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    connectInterceptor: ConnectivityInterceptor
) : BaseRepository(connectInterceptor), LoginRepository {

    override suspend fun getAuthUser(): Result<Exception, User?> = Result.build {
        auth.currentUser?.toUser
    }

    override suspend fun signOutCurrentUser(): Result<Exception, Unit> = Result.build {
        auth.signOut()
    }

    override suspend fun signInGoogleUser(idToken: String): Result<Exception, Unit> = Result.build {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        awaitTaskCompletable(auth.signInWithCredential(credential))
    }

    override suspend fun isUserExist(): Result<Exception, Boolean?> = Result.build {
        lazyIsUserExist.await()
    }

    override suspend fun updateLocalUser(user: User): Result<Exception, Boolean> = Result.build {
        if (lazyIsUserExist.await()) return@build false
        local.upsert(user.toRoomUser)
        true
    }

    override suspend fun deleteLocalUser(): Result<Exception, Unit> = Result.build {
        local.deleteUser()
    }

    private val lazyIsUserExist by lazyDeferred {
        when (val result = Result.build {
            local.isExist()
        }) {
            is Result.Value -> result.value
            is Result.Error -> false
        }
    }
}