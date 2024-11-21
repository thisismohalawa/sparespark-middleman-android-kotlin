package sparespark.middleman.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.DELAY_LOGIN
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.user.User
import sparespark.middleman.data.repository.UserRepository

class HomeActivityViewModel(
    private val userRepo: UserRepository
) : BaseViewModel<HomeActivityEvent>() {

    private val userState = MutableLiveData<User?>()

    internal val loginAttempt = MutableLiveData<Unit>()

    override fun handleEvent(event: HomeActivityEvent) {
        when (event) {
            is HomeActivityEvent.OnStartCheckUser -> getUser()
        }
    }

    private fun getUser() = viewModelScope.launch {
        when (val result = userRepo.getLocalUser()) {
            is Result.Error -> {
                delay(DELAY_LOGIN)
                moveToLoginView()
            }

            is Result.Value -> {
                userState.value = result.value
            }
        }
    }


    private fun moveToLoginView() {
        loginAttempt.value = Unit
    }
}