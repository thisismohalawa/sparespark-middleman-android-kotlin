package sparespark.middleman.cartlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.Result
import sparespark.middleman.common.toOrderTitleFromProductList
import sparespark.middleman.model.Product
import sparespark.middleman.model.repository.CartRepository

class CartListViewModel(
    private val cartRepo: CartRepository
) : BaseViewModel<CartListEvent>() {

    internal val bottomSheetViewState = MutableLiveData<Int>()
    internal val actionBackStack = MutableLiveData<Unit>()
    internal val buyAttempt = MutableLiveData<Unit>()

    private val productListState = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = productListState

    private val totalPriceState = MutableLiveData<Double?>()
    val totalPrice: MutableLiveData<Double?> get() = totalPriceState

    private val updateState = MutableLiveData<Unit>()
    val updated: LiveData<Unit> get() = updateState

    override fun handleEvent(event: CartListEvent) {
        when (event) {
            is CartListEvent.GetCartProducts -> getProducts()
            is CartListEvent.OnClearItemClick -> removeProductFromCart(event.position)
            is CartListEvent.OnImgItemClick -> startProductExploring(
                product = productListState.value?.get(
                    event.position
                )
            )

            is CartListEvent.OnBackButtonPressed -> startBackPressedAction()
            is CartListEvent.OnSupportTextClick -> startContactAction()

            is CartListEvent.OnBuyButtonClick -> startBuyAction()
        }
    }

    private fun isCartEmpty(): Boolean = productListState.value.isNullOrEmpty()

    private fun startBuyAction() {
        if (isCartEmpty()) {
            showError(R.string.ur_bag_is_empty)
            return
        }
        buyAttempt.value = Unit
    }

    private fun startContactAction() {
        if (isCartEmpty()) {
            showError(R.string.ur_bag_is_empty)
            return
        }
        startSupportContact(productListState.value?.toOrderTitleFromProductList())
    }

    private fun getProducts() = viewModelScope.launch {
        showLoading()
        when (val result = cartRepo.getCartList()) {
            is Result.Error -> {
                showError(R.string.can_not_read_local_data)
                showZeroTotalPrice()
            }

            is Result.Value -> {
                productListState.value = result.value
                getCartTotalPrice()
            }
        }
        hideLoading()
    }

    private fun getCartTotalPrice() = viewModelScope.launch {
        if (isCartEmpty()) {
            showZeroTotalPrice()
            return@launch
        }
        when (val result = cartRepo.getTotalProductsPrice()) {
            is Result.Error -> showZeroTotalPrice()
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

    private fun startBackPressedAction() {
        fun updateBottomSheetToHideState() {
            bottomSheetViewState.value = BottomSheetBehavior.STATE_HIDDEN
        }

        if (bottomSheetViewState.value != BottomSheetBehavior.STATE_HIDDEN)
            updateBottomSheetToHideState()
        else actionBackStack.value = Unit
    }

    private fun showZeroTotalPrice() {
        totalPriceState.value = 0.0
    }
}