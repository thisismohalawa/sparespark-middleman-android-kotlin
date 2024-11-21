package sparespark.middleman.productlist.buildlogic

import android.app.Application
import sparespark.middleman.productlist.BaseProductListInjector

class ProductListViewInjector(
    app: Application
) : BaseProductListInjector(app) {
    fun provideViewModelFactory() = ProductListViewModelFactory(
        getProductRepository()
    )
}