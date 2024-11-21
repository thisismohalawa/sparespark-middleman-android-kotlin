package sparespark.middleman.brandlist.buildlogic

import android.app.Application
import sparespark.middleman.brandlist.BaseBrandListInjector

class BrandListInjector(application: Application) : BaseBrandListInjector(application) {
    fun provideViewModelFactory(): BrandListViewModelFactory =
        BrandListViewModelFactory(getBrandRepository())
}