package sparespark.middleman.product.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.NO_INTERNET_CONNECTION
import sparespark.middleman.common.Result
import sparespark.middleman.model.Product
import sparespark.middleman.model.repository.ProductRepository

class ProductListViewModel(
    private val productRepo: ProductRepository,
) : BaseViewModel<ProductListEvent>() {

    private val productListState = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = productListState

    override fun handleEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.GetProducts -> getProducts()
            is ProductListEvent.OnProductClicked -> startProductExploring(event.product)
        }
    }

    private fun getProducts() = viewModelScope.launch {
        showLoading()
        when (val result = productRepo.getProducts()) {
            is Result.Error -> when (result.error.message) {
                NO_INTERNET_CONNECTION -> {
                    showError(R.string.no_internet)
                    getLocalProducts()
                }

                else -> showError(R.string.error_load_products)
            }

            is Result.Value -> productListState.value = result.value
        }
        hideLoading()
    }

    private suspend fun getLocalProducts() {
        when (val result = productRepo.getLocalProducts()) {
            is Result.Error -> showError(R.string.can_not_read_local_data)
            is Result.Value -> productListState.value = result.value
        }
    }
}