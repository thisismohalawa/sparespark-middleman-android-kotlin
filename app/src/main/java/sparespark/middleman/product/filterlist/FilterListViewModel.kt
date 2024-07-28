package sparespark.middleman.product.filterlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.NO_INTERNET_CONNECTION
import sparespark.middleman.common.Result
import sparespark.middleman.model.Product
import sparespark.middleman.model.repository.ProductRepository

class FilterListViewModel(
    private val productRepo: ProductRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<FilterListEvent>() {

    private val navBrandId: String = savedStateHandle["brandId"] ?: ""

    private val productListState = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = productListState

    override fun handleEvent(event: FilterListEvent) {
        when (event) {
            is FilterListEvent.GetProducts -> if (navBrandId.isNotBlank())
                getBrandsProducts() else getProducts()

            is FilterListEvent.OnProductClicked -> startProductExploring(event.product)
            is FilterListEvent.OnSupportButtonClick -> startSupportContact("")
        }
    }

    private fun getProducts() = viewModelScope.launch {
        showLoading()
        when (val result = productRepo.getProducts()) {
            is Result.Error -> when (result.error.message) {
                NO_INTERNET_CONNECTION -> showError(R.string.no_internet)

                else -> showError(R.string.error_load_products)
            }

            is Result.Value -> productListState.value = result.value
        }
        hideLoading()
    }

    private fun getBrandsProducts() = viewModelScope.launch {
        showLoading()
        when (val result = productRepo.getProductsByBrandId(
            brandId = navBrandId
        )) {
            is Result.Error -> when (result.error.message) {
                NO_INTERNET_CONNECTION -> showError(R.string.no_internet)

                else -> showError(R.string.error_load_products)
            }

            is Result.Value -> productListState.value = result.value
        }
        hideLoading()
    }

}