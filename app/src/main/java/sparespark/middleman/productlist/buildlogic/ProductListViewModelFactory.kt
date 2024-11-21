package sparespark.middleman.productlist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.data.repository.ProductRepository
import sparespark.middleman.productlist.ProductListViewModel

class ProductListViewModelFactory(
    private val productRepo: ProductRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ProductListViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            ProductListViewModel(productRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}
