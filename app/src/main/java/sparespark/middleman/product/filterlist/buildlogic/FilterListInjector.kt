package sparespark.middleman.product.filterlist.buildlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector

class FilterListInjector(application: Application) :
    BaseInjector(application) {

    fun provideFilterListViewModelFactory(): FilterListViewModelFactory =
        FilterListViewModelFactory(
            getProductRepository()
        )
}