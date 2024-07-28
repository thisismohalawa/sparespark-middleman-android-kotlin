package sparespark.middleman.cartlist.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.cartlist.CartListViewModel
import sparespark.middleman.model.repository.CartRepository

class CartListViewModelFactory(
    private val cartRepo: CartRepository
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CartListViewModel(cartRepo) as T
    }
}