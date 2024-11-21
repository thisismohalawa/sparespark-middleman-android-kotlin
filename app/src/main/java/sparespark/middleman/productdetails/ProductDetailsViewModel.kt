package sparespark.middleman.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.model.product.RelatedColor
import sparespark.middleman.data.model.product.RelatedUrl
import sparespark.middleman.data.repository.CartRepository
import sparespark.middleman.data.repository.ProductRepository

class ProductDetailsViewModel(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProductDetailEvent>() {

    private val navProductId: String? = savedStateHandle["productId"]

    internal val cartState = MutableLiveData<Boolean?>()

    private val productState = MutableLiveData<Product>()
    val product: LiveData<Product> get() = productState

    private val colorListState = MutableLiveData<List<RelatedColor>>()
    val colorList: LiveData<List<RelatedColor>> get() = colorListState

    private val relatedListState = MutableLiveData<List<RelatedUrl>>()
    val relatedList: LiveData<List<RelatedUrl>> get() = relatedListState

    override fun handleEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.OnStartGetProduct -> {
                navProductId?.let { productId ->
                    getProduct(productId)
                    checkIfCartProduct(productId)
                }

            }

            is ProductDetailEvent.OnCartTextClick -> cartBtnClickAction()
            is ProductDetailEvent.OnRelatedItemClick -> {
                relatedListState.value?.get(event.position)?.id?.let {
                    getProduct(it)
                    checkIfCartProduct(it)
                }
            }

            is ProductDetailEvent.OnColorItemClick -> {
                colorListState.value?.get(event.position)?.id?.let {
                    getProduct(it)
                    checkIfCartProduct(it)
                }
            }
        }
    }

    private fun getProduct(productId: String) = viewModelScope.launch {
        showLoading()
        delay(300)
        when (val result = productRepo.getProductById(productId)) {
            is Result.Error -> result.error.message.checkExceptionMsg(error = {
                showError(R.string.error_retrieve_data)
            })

            is Result.Value -> {
                productState.value = result.value
                getRelatedItems()
                getColorsItems()
            }
        }
        hideLoading()
    }

    private fun getRelatedItems() {
        if (productState.value?.related?.isNotEmpty() == true)
            relatedListState.value = productState.value!!.related
    }

    private fun getColorsItems() {
        if (productState.value?.colors?.isNotEmpty() == true)
            colorListState.value = productState.value!!.colors
    }

    private fun checkIfCartProduct(productId: String) = viewModelScope.launch {
        when (val result = cartRepo.isCarted(productId)) {
            is Result.Error -> {
                showError(R.string.cannot_read_local_data)
                cartState.value = false
            }

            is Result.Value -> cartState.value = result.value
        }
    }

    private fun cartBtnClickAction() {
        if (cartState.value == true) removeProductFromCart()
        else addProductToCart()
    }

    private fun addProductToCart() = viewModelScope.launch {
        when (productState.value?.let {
            cartRepo.addToCartList(
                product = it
            )
        }) {
            is Result.Error -> showError(R.string.cannot_update_local_entries)

            is Result.Value -> cartState.value = true

            else -> Unit
        }
    }

    private fun removeProductFromCart() = viewModelScope.launch {
        when (productState.value?.creationDate?.let {
            cartRepo.removeFromCartList(
                productId = it
            )
        }) {
            is Result.Error -> showError(R.string.cannot_update_local_entries)

            is Result.Value -> cartState.value = false

            else -> Unit
        }

    }

}