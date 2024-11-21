package sparespark.middleman.auth.signup

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.auth.AuthEvent
import sparespark.middleman.auth.BaseAuthViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.LoginResult
import sparespark.middleman.data.repository.LoginRepository

class SignUpViewModel(
    private val loginRepo: LoginRepository
) : BaseAuthViewModel() {

    override fun handleEvent(event: AuthEvent<LoginResult>) {
        if (event is AuthEvent.OnSignupBtnClick) {
            signup(event.email, event.pass)
        }
    }

    private fun signup(email: String, pass: String) = viewModelScope.launch {
        if (!isValidInputs(email, pass)) return@launch
        showLoading()
        when (val result = loginRepo.signUpWithEmailAndPass(email, pass)) {
            is Result.Error -> result.error.message.checkExceptionMsg(
                error = { showError(R.string.unable_to_sign_up) }
            )

            is Result.Value -> authUpdated()
        }
        hideLoading()
    }
}
