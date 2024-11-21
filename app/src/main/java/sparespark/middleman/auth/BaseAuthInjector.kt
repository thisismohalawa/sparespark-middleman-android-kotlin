package sparespark.middleman.auth

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import sparespark.middleman.core.base.BaseInjector
import sparespark.middleman.data.implementation.LoginRepositoryImpl

open class BaseAuthInjector(
    app: Application
) : BaseInjector(app) {

    protected fun getLoginRepository() = LoginRepositoryImpl(
        FirebaseAuth.getInstance()
    )
}
