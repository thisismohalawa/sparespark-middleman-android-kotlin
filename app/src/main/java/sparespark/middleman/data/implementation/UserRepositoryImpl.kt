package sparespark.middleman.data.implementation

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sparespark.middleman.core.DATABASE_URL
import sparespark.middleman.core.REF_DATABASE
import sparespark.middleman.core.REF_TEAM_LIST
import sparespark.middleman.core.awaitTaskCompletable
import sparespark.middleman.core.awaitTaskResult
import sparespark.middleman.core.mapping.toUser
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.connectivity.ConnectivityInterceptor
import sparespark.middleman.data.implementation.base.BaseLocalUserPreferenceRepository
import sparespark.middleman.data.model.user.RemoteUser
import sparespark.middleman.data.model.user.User
import sparespark.middleman.data.repository.UserRepository
import sparespark.middleman.data.room.user.UserDao

class UserRepositoryImpl(
    private val remote: DatabaseReference = FirebaseDatabase.getInstance(DATABASE_URL)
        .getReference(REF_DATABASE).child(REF_TEAM_LIST),
    local: UserDao,
    connectInterceptor: ConnectivityInterceptor
) : BaseLocalUserPreferenceRepository(
    userDao = local,
    connectInterceptor = connectInterceptor
), UserRepository {

    override suspend fun getLocalUser(): Result<Exception, User?> = currentUser()

    override suspend fun getRemoteUser(): Result<Exception, User?> = Result.build {
        checkNetConnection()
        checkUserAuthorizedCredentials()
        val task: DataSnapshot = awaitTaskResult(remote.child(currentUserId()!!).get())
        task.getValue(RemoteUser::class.java)?.toUser
    }

    override suspend fun updateLocalUser(user: User): Result<Exception, Unit> =
        updateCurrentUser(user)

    override suspend fun pushUserToRemoteServer(user: User): Result<Exception, Unit> =
        Result.build {
            checkNetConnection()
            awaitTaskCompletable(
                remote.child(user.uid).setValue(user)
            )
        }


    override suspend fun signOutCurrentUser(): Result<Exception, Unit> = Result.build {
        checkNetConnection()
        checkUserAuthorizedCredentials()
        FirebaseAuth.getInstance().signOut()
        deleteCurrentUser()
    }


    override suspend fun checkIfPushRequired(): Result<Exception, Boolean> = Result.build {
        !isUserExist()
    }
}