package sparespark.middleman.product.filterlist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import sparespark.middleman.model.repository.ProductRepository
import sparespark.middleman.product.filterlist.FilterListViewModel

class FilterListViewModelFactory(
    private val productRepo: ProductRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FilterListViewModel(productRepo, extras.createSavedStateHandle()) as T
    }
}