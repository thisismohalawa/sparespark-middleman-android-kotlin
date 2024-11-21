package sparespark.middleman.brandlist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.brandlist.BrandListViewModel
import sparespark.middleman.data.repository.BrandRepository

class BrandListViewModelFactory(
    private val brandRepo: BrandRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(BrandListViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            BrandListViewModel(brandRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}