package sparespark.middleman.home.buildlogic

import android.app.Application
import sparespark.middleman.core.base.BaseInjector

class HomeActivityInjector(
    app: Application
) : BaseInjector(app) {
    fun provideViewModelFactory() = HomeActivityViewModelFactory(getUserRepository())
}
