package sparespark.middleman.login.buildlogic

import android.app.Application
import com.google.firebase.FirebaseApp
import sparespark.middleman.common.BaseInjector
import sparespark.middleman.model.implementation.LoginRepositoryImpl
import sparespark.middleman.model.preference.BrandPreferenceImpl
import sparespark.middleman.model.preference.LoginPreferenceImpl
import sparespark.middleman.model.repository.LoginRepository
import sparespark.middleman.model.room.XDatabase

class LoginInjector(application: Application) : BaseInjector(application) {

    init {
        FirebaseApp.initializeApp(application)
    }

    fun provideLoginViewModelFactory(): LoginViewModelFactory =
        LoginViewModelFactory(
            loginRepo = getLoginRepository()
        )
}