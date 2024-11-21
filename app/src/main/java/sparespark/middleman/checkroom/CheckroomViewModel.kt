package sparespark.middleman.checkroom

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.mapping.toOrderTitleFromProductList
import sparespark.middleman.core.wrapper.Event
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.repository.CartRepository

class CheckroomViewModel(
    private val cartRepo: CartRepository
) : BaseViewModel<CheckroomEvent>() {

    internal val supportAttempt = MutableLiveData<Event<String>>()

    private val productListState = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = productListState

    private val totalPriceState = MutableLiveData<Double>()
    val totalPrice: MutableLiveData<Double> get() = totalPriceState

    private val updateState = MutableLiveData<Unit>()
    val updated: LiveData<Unit> get() = updateState

    private val editProductState = MutableLiveData<Event<String>>()
    val editProduct: LiveData<Event<String>> = editProductState

    override fun handleEvent(event: CheckroomEvent) {
        when (event) {
            is CheckroomEvent.GetProducts -> {
                getProducts()
            }

            is CheckroomEvent.OnImgClearItemClick -> removeProductFromCart(event.position)

            is CheckroomEvent.OnImgProductItemClick -> startProductExploring(event.position)
            is CheckroomEvent.OnWhatsAppBuyBtnClick -> startContactAction()

        }
    }

    private fun isCartEmpty(): Boolean = productListState.value.isNullOrEmpty()

    private fun getProducts() = viewModelScope.launch {
        showLoading()
        when (val result = cartRepo.getCartList()) {
            is Result.Error -> showError(R.string.cannot_read_local_data)
            is Result.Value -> {
                productListState.value = result.value
                getCartTotalPrice()
            }
        }
        hideLoading()
    }

    private fun getCartTotalPrice() = viewModelScope.launch {
        if (isCartEmpty()) {
            totalPriceState.value = 0.0
            return@launch
        }
        when (val result = cartRepo.getTotalProductsPrice()) {
            is Result.Error -> totalPriceState.value = 0.0
            is Result.Value -> totalPriceState.value = result.value
        }
    }

    private fun removeProductFromCart(position: Int) = viewModelScope.launch {
        when (cartRepo.removeFromCartList(
            productId = productListState.value?.get(position)?.creationDate.toString()
        )) {
            is Result.Error -> showError(R.string.cannot_update_local_entries)
            is Result.Value -> updateState.value = Unit
        }
    }

    private fun startContactAction() {
        if (isCartEmpty()) return
        supportAttempt.value = Event(productListState.value?.toOrderTitleFromProductList() ?: "")
    }

    protected fun startProductExploring(position: Int) {
        productListState.value?.get(position)?.creationDate?.let {
            editProductState.value = Event(it)
        }
    }
}