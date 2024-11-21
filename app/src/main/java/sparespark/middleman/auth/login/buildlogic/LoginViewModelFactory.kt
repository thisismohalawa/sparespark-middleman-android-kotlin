package sparespark.middleman.auth.login.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.auth.login.LoginViewModel
import sparespark.middleman.data.repository.LoginRepository
import sparespark.middleman.data.repository.UserRepository

class LoginViewModelFactory(
    private val loginRepo: LoginRepository,
    private val userRepo: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(LoginViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            LoginViewModel(loginRepo, userRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}