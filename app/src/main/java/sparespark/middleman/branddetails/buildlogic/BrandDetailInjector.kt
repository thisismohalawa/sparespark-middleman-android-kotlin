package sparespark.middleman.branddetails.buildlogic

import android.app.Application
import sparespark.middleman.brandlist.BaseBrandListInjector

class BrandDetailInjector(application: Application) :
    BaseBrandListInjector(application) {
    fun provideViewModelFactory(): BrandDetailsViewModelFactory =
        BrandDetailsViewModelFactory(getBrandRepository())
}