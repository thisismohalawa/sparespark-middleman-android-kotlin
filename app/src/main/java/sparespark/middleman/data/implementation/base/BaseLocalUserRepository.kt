package sparespark.middleman.data.implementation.base

import sparespark.middleman.core.mapping.toRoomUser
import sparespark.middleman.core.mapping.toUser
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.room.user.UserDao
import sparespark.middleman.data.model.user.User
import sparespark.middleman.core.lazyDeferred

open class BaseLocalUserRepository(
    private val userDao: UserDao
) {
    protected suspend fun isUserExist() = deferredUserExist.await()

    protected suspend fun currentUserId(): String? = deferredUserId.await()

    protected suspend fun currentUser(): Result<Exception, User?> = Result.build {
        if (isUserExist()) deferredUser.await() else null
    }

    protected suspend fun updateCurrentUser(user: User): Result<Exception, Unit> = Result.build {
        userDao.upsert(user.toRoomUser)
        Unit
    }

    protected suspend fun deleteCurrentUser(): Result<Exception, Unit> = Result.build {
        userDao.deleteUser()
    }

    private val deferredUserExist by lazyDeferred {
        when (val result = Result.build {
            userDao.isExist()
        }) {
            is Result.Value -> result.value
            is Result.Error -> false
        }
    }
    private val deferredUserId by lazyDeferred {
        when (val result = Result.build {
            userDao.getUserId()
        }) {
            is Result.Value -> result.value
            is Result.Error -> null
        }
    }
    private val deferredUser by lazyDeferred {
        when (val result = Result.build {
            userDao.getUser()?.toUser
        }) {
            is Result.Value -> result.value
            is Result.Error -> null
        }
    }
}