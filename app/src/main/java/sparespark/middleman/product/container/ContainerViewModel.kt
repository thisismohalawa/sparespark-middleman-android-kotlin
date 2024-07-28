package sparespark.middleman.product.container

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.Event
import sparespark.middleman.common.Result
import sparespark.middleman.model.preference.LoginPreference
import sparespark.middleman.model.repository.LoginRepository
import sparespark.middleman.product.ProductActivityEvent

class ContainerViewModel(
    private val loginRepo: LoginRepository,
    private val loginPref: LoginPreference
) : BaseViewModel<ProductActivityEvent>() {

    internal val loginAttempt = MutableLiveData<Event<Unit>>()

    override fun handleEvent(event: ProductActivityEvent) {
        when (event) {
            is ProductActivityEvent.OnStartCheckUser -> checkUser()
        }
    }

    private fun checkUser() = viewModelScope.launch {
        when (val result = loginRepo.isUserExist()) {
            is Result.Error -> showError(R.string.cant_find_auth_user)

            is Result.Value -> if (result.value == false) {
                if (loginPref.isLoginAttemptNeeded()) {
                    delay(5000)
                    loginAttempt.value = Event(Unit)
                    loginPref.updateLoginAttemptTimeToNow()
                }
            }
        }
    }
}