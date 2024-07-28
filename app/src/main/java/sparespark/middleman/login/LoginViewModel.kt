package sparespark.middleman.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.Result
import sparespark.middleman.common.SIGN_IN_REQUEST_CODE
import sparespark.middleman.common.UIResource
import sparespark.middleman.model.LoginResult
import sparespark.middleman.model.User
import sparespark.middleman.model.preference.LoginPreference
import sparespark.middleman.model.repository.LoginRepository

class LoginViewModel(
    private val loginRepo: LoginRepository,
) : BaseViewModel<LoginEvent<LoginResult>>() {

    private val updatedState = MutableLiveData<Boolean>()
    val updated: LiveData<Boolean> get() = updatedState

    private val userState = MutableLiveData<User?>()
    internal val authAttempt = MutableLiveData<Unit>()
    internal val signInStatusText = MutableLiveData<UIResource>()
    internal val deleteTextVisibleStatus = MutableLiveData<Boolean>()
    internal val authButtonText = MutableLiveData<UIResource>()

    override fun handleEvent(event: LoginEvent<LoginResult>) {
        showLoading()
        when (event) {
            is LoginEvent.OnStart -> getAuthUser()
            is LoginEvent.OnAuthButtonClick -> onAuthButtonClick()
            is LoginEvent.OnGoogleSignInResult -> onSignInResult(event.result)
            is LoginEvent.OnDeleteAccTextClick -> {
                signOutUser()
                deleteUser()
            }
        }
    }

    private fun getAuthUser() = viewModelScope.launch {
        when (val result = loginRepo.getAuthUser()) {
            is Result.Error -> {
                showError(R.string.cant_find_auth_user)
                showSignedOutState()
            }

            is Result.Value -> result.value.let {
                hideLoading()
                userState.value = it
                if (it != null) {
                    showSignedInState()
                    updateLocalUser(it)
                } else showSignedOutState()
            }
        }
    }


    private suspend fun updateLocalUser(user: User) = viewModelScope.launch {
        when (val result = loginRepo.updateLocalUser(user)) {
            is Result.Value -> updatedState.value = result.value
            is Result.Error -> showError(R.string.cannot_update_local_entries)
        }
    }

    private fun onAuthButtonClick() {
        if (userState.value == null) authAttempt.value = Unit
        else signOutUser()
    }


    private fun signOutUser() = viewModelScope.launch {
        when (loginRepo.signOutCurrentUser()) {
            is Result.Error -> showError(R.string.unable_to_sign_out)
            is Result.Value -> {
                userState.value = null
                hideLoading()
                showSignedOutState()
            }
        }
    }

    private fun deleteUser() = viewModelScope.launch {
        when (loginRepo.deleteLocalUser()) {
            is Result.Error -> showError(R.string.cannot_update_local_entries)
            is Result.Value -> updatedState.value = true
        }
    }

    private fun onSignInResult(result: LoginResult) = viewModelScope.launch {
        if (result.requestCode == SIGN_IN_REQUEST_CODE &&
            result.userToken != null
        ) {
            val createGoogleUserResult = loginRepo.signInGoogleUser(result.userToken)

            if (createGoogleUserResult is Result.Value) getAuthUser()
            else showError(R.string.unable_to_sign_in)

        } else
            showError(R.string.unable_to_sign_in)
    }


    private fun showSignedInState() {
        signInStatusText.value = UIResource.StringResource(R.string.signed_in_state)
        authButtonText.value = UIResource.StringResource(R.string.sign_out)
        deleteTextVisibleStatus.value = true
    }

    private fun showSignedOutState() {
        signInStatusText.value = UIResource.StringResource(R.string.signed_out_state)
        authButtonText.value = UIResource.StringResource(R.string.sign_in)
        deleteTextVisibleStatus.value = false
    }
}