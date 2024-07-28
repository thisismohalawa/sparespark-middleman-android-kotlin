package sparespark.middleman.product.productlist.buildlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector

class ProductListInjector(application: Application) :
    BaseInjector(application) {

    fun provideProductListViewModelFactory(): ProductListViewModelFactory =
        ProductListViewModelFactory(
            getProductRepository()
        )
}