package sparespark.middleman.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.isEmailAddress
import sparespark.middleman.core.isValidPasswordLength
import sparespark.middleman.data.model.LoginResult

abstract class BaseAuthViewModel : BaseViewModel<AuthEvent<LoginResult>>() {

    internal val emailTxtValidateState = MutableLiveData<Unit>()
    internal val passwordTxtValidateState = MutableLiveData<Unit>()

    private val updatedState = MutableLiveData<Unit>()
    val updated: LiveData<Unit> get() = updatedState

    protected fun authUpdated() {
        updatedState.value = Unit
    }

    protected fun isValidInputs(email: String, pass: String): Boolean =
        if (!email.isEmailAddress()) {
            emailTxtValidateState.value = Unit
            false
        } else if (!pass.isValidPasswordLength()) {
            passwordTxtValidateState.value = Unit
            false
        } else true
}