package sparespark.middleman.userprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.DEACTIVATED
import sparespark.middleman.core.DELAY_LOGIN
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.user.User
import sparespark.middleman.data.repository.UserRepository

abstract class UserViewModel(
    private val userRepo: UserRepository
) : BaseViewModel<UserEvent>() {

    private val userState = MutableLiveData<User?>()
    val user: LiveData<User?> get() = userState

    private val updatedState = MutableLiveData<Unit>()
    val updated: LiveData<Unit> get() = updatedState

    protected fun isUserExist() = userState.value?.uid?.isNotBlank() == true

    protected fun getUser(followAction: (() -> Unit)? = null) = viewModelScope.launch {
        when (val result = userRepo.getLocalUser()) {
            is Result.Error -> showError(R.string.cannot_read_local_data)

            is Result.Value -> {
                userState.value = result.value ?: tempUser()
                followAction?.invoke()
            }
        }
    }

    protected suspend fun userUpdated() {
        delay(DELAY_LOGIN)
        updatedState.value = Unit
    }

    private fun tempUser() = User("", "UserName", "Temporary.", DEACTIVATED)

}