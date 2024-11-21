package sparespark.middleman.productdetails.buildlogic

import android.app.Application
import sparespark.middleman.productlist.BaseProductListInjector

class ProductDetailInjector(application: Application) :
    BaseProductListInjector(application) {
    fun provideViewModelFactory(): ProductDetailsViewModelFactory =
        ProductDetailsViewModelFactory(
            getProductRepository(),
            getCartRepository()
        )
}