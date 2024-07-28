package sparespark.middleman.product.productdetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.ErrorProduct
import sparespark.middleman.common.NO_INTERNET_CONNECTION
import sparespark.middleman.common.Result
import sparespark.middleman.model.IColor
import sparespark.middleman.model.IRelated
import sparespark.middleman.model.Product
import sparespark.middleman.model.repository.BrandRepository
import sparespark.middleman.model.repository.CartRepository
import sparespark.middleman.model.repository.ProductRepository

class ProductViewModel(
    private val productRepo: ProductRepository,
    private val branRepo: BrandRepository,
    private val cartRepo: CartRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<ProductDetailEvent>() {

    private val navProduct: Product = savedStateHandle["product"] ?: ErrorProduct

    internal val cartState = MutableLiveData<Boolean?>()
    internal val brandToolbarText = MutableLiveData<String>()

    private val productState = MutableLiveData<Product>()
    val product: LiveData<Product> get() = productState

    private val colorListState = MutableLiveData<List<IColor>>()
    val colorList: LiveData<List<IColor>> get() = colorListState

    private val relatedListState = MutableLiveData<List<IRelated>>()
    val relatedList: LiveData<List<IRelated>> get() = relatedListState

    override fun handleEvent(event: ProductDetailEvent) {
        when (event) {
            is ProductDetailEvent.OnStartGetProduct -> {
                productState.value = navProduct
                getRelatedItems()
                getColorsItems()
                checkIfCartProduct(navProduct.creationDate)
            }

            is ProductDetailEvent.GetBrandName -> getBrandName()
            is ProductDetailEvent.OnCartTextClick -> cartBtnClickAction()
            is ProductDetailEvent.OnRelatedItemClick -> {
                relatedListState.value?.get(event.position)?.id?.let {
                    getProduct(
                        it
                    )
                }
            }

            is ProductDetailEvent.OnColorItemClick -> {
                colorListState.value?.get(event.position)?.id?.let {
                    getProduct(
                        it
                    )
                }
            }
        }
    }

    private fun getProduct(
        productId: String,
    ) = viewModelScope.launch {
        showLoading()
        when (val result = productRepo.getProductById(productId)) {
            is Result.Error -> when (result.error.message) {
                NO_INTERNET_CONNECTION -> showError(R.string.no_internet)
                else -> showError(R.string.error_load_products)
            }

            is Result.Value -> {
                productState.value = result.value
                getRelatedItems()
                getColorsItems()
                checkIfCartProduct(result.value.creationDate)
            }
        }
        hideLoading()
    }

    private fun getBrandName() = viewModelScope.launch {
        when (val result = productState.value?.let { branRepo.getBrandNameById(it.brandId) }) {
            is Result.Error -> brandToolbarText.value = "BrandX"
            is Result.Value -> brandToolbarText.value = result.value
            null -> brandToolbarText.value = "BrandX"
        }
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
                showError(R.string.can_not_read_remote_data)
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
        if (productState.value?.activated == false) {
            showError(R.string.unavailable)
            return@launch
        }
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