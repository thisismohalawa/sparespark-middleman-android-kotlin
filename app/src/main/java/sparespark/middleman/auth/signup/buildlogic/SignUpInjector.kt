package sparespark.middleman.auth.signup.buildlogic

import android.app.Application
import com.google.firebase.FirebaseApp
import sparespark.middleman.auth.BaseAuthInjector

class SignUpInjector(
    app: Application
) : BaseAuthInjector(app) {
    fun provideViewModelFactory() = SignUpViewModelFactory(
        getLoginRepository()
    )
}
