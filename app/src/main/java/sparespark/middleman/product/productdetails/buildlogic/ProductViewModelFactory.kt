package sparespark.middleman.product.productdetails.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import sparespark.middleman.model.repository.BrandRepository
import sparespark.middleman.model.repository.CartRepository
import sparespark.middleman.model.repository.ProductRepository
import sparespark.middleman.product.productdetails.ProductViewModel

class ProductViewModelFactory(
    private val productRepo: ProductRepository,
    private val branRepo: BrandRepository,
    private val cartRepo: CartRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return ProductViewModel(
            productRepo,
            branRepo,
            cartRepo,
            extras.createSavedStateHandle()
        ) as T
    }
}