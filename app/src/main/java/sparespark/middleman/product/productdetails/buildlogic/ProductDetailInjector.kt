package sparespark.middleman.product.productdetails.buildlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector

class ProductDetailInjector(application: Application) :
    BaseInjector(application) {

    fun provideProductViewModelFactory(): ProductViewModelFactory =
        ProductViewModelFactory(
            getProductRepository(),
            getBrandRepository(),
            getCartRepository()
        )
}