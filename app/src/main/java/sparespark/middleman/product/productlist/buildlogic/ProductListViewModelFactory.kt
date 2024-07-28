package sparespark.middleman.product.productlist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.model.repository.ProductRepository
import sparespark.middleman.product.productlist.ProductListViewModel

class ProductListViewModelFactory(
    private val productRepo: ProductRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductListViewModel(productRepo) as T
    }
}