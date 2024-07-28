package sparespark.middleman.product.container.buildlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector
import sparespark.middleman.model.preference.LoginPreferenceImpl

class ContainerInjector(application: Application) :
    BaseInjector(application) {

    fun provideContainerViewModelFactory(): ContainerViewModelFactory =
        ContainerViewModelFactory(
            loginRepo = getLoginRepository(),
            loginPref = LoginPreferenceImpl(getApplication())
        )
}