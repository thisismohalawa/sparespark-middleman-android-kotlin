package sparespark.middleman.auth.login.buildlogic

import android.app.Application
import sparespark.middleman.auth.BaseAuthInjector

class LoginInjector(application: Application) : BaseAuthInjector(application) {
    fun provideViewModelFactory(): LoginViewModelFactory = LoginViewModelFactory(
        loginRepo = getLoginRepository(),
        userRepo = getUserRepository()
    )
}