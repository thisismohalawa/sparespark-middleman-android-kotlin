package sparespark.middleman.checkroom.buildlogic

import android.app.Application
import sparespark.middleman.core.base.BaseInjector

class CheckroomListInjector(application: Application) : BaseInjector(application) {
    fun provideViewModelFactory(): CheckroomViewModelFactory =
        CheckroomViewModelFactory(
            getCartRepository()
        )
}