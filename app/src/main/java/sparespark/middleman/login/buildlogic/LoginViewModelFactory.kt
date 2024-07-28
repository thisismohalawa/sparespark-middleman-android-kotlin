package sparespark.middleman.login.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.login.LoginViewModel
import sparespark.middleman.model.repository.LoginRepository

class LoginViewModelFactory(
    private val loginRepo: LoginRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(loginRepo) as T
    }
}