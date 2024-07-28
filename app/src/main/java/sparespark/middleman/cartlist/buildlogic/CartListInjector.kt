package sparespark.middleman.cartlist.buildlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector

class CartListInjector(application: Application) : BaseInjector(application) {

    fun provideCartListViewModelFactory(): CartListViewModelFactory =
        CartListViewModelFactory(
            getCartRepository()
        )
}