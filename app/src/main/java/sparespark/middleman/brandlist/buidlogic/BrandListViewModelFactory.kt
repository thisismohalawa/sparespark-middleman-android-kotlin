package sparespark.middleman.brandlist.buidlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.brandlist.BrandListViewModel
import sparespark.middleman.model.repository.BrandRepository

class BrandListViewModelFactory(
    private val brandRepo: BrandRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BrandListViewModel(brandRepo) as T
    }
}