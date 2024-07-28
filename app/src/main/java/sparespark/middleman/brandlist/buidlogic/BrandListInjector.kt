package sparespark.middleman.brandlist.buidlogic

import android.app.Application
import sparespark.middleman.common.BaseInjector
import sparespark.middleman.model.room.XDatabase
import sparespark.middleman.model.implementation.BrandRepositoryImpl
import sparespark.middleman.model.preference.BrandPreferenceImpl

class BrandListInjector(application: Application) : BaseInjector(application) {

    fun provideBrandListViewModelFactory(): BrandListViewModelFactory =
        BrandListViewModelFactory(
            getBrandRepository()
        )
}