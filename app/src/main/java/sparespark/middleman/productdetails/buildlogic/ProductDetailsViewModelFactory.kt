package sparespark.middleman.productdetails.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import sparespark.middleman.data.repository.CartRepository
import sparespark.middleman.data.repository.ProductRepository
import sparespark.middleman.productdetails.ProductDetailsViewModel

class ProductDetailsViewModelFactory(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            ProductDetailsViewModel(productRepo, cartRepo, extras.createSavedStateHandle()) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}