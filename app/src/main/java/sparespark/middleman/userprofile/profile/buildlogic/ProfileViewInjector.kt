package sparespark.middleman.userprofile.profile.buildlogic

import android.app.Application
import sparespark.middleman.core.base.BaseInjector

class ProfileViewInjector(
    app: Application
) : BaseInjector(app) {
    fun provideViewModelFactory() = ProfileViewModelFactory(userRepo = getUserRepository())
}
