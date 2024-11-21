package sparespark.middleman.productlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.menu.MENU_BRAND
import sparespark.middleman.core.menu.getCategoryMenu
import sparespark.middleman.core.wrapper.Event
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.category.CategoryMenu
import sparespark.middleman.data.model.category.CategorySelect
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.data.repository.ProductRepository

class ProductListViewModel(
    private val productRepo: ProductRepository
) : BaseViewModel<ProductListEvent>() {

    internal val navigateToBrandListAttempt = MutableLiveData<Event<Unit>>()
    internal val updateCategoryMenuSelectAttempt = MutableLiveData<List<CategorySelect>>()

    private val productListState = MutableLiveData<List<Product>>()
    val productList: LiveData<List<Product>> get() = productListState

    private val categoryListState = MutableLiveData<List<CategoryMenu>>()
    val categoryList: LiveData<List<CategoryMenu>> get() = categoryListState

    private val editProductState = MutableLiveData<Event<String>>()
    val editProduct: LiveData<Event<String>> = editProductState

    override fun handleEvent(event: ProductListEvent) {
        when (event) {
            is ProductListEvent.OnViewStart -> {
                getProductList()
                categoryListState.value = getCategoryMenu()
            }

            is ProductListEvent.OnMenuCategoryClicked -> onCategoryMenuListClick(event.categoryId)
            is ProductListEvent.OnProductItemClick -> productListState.value?.let {
                editProductState.value = Event(it[event.position].creationDate)
            }
        }
    }

    private fun getLocalList() = viewModelScope.launch {
        val result = productRepo.getProductList(localOnly = Unit)
        if (result is Result.Value) productListState.value = result.value.asReversed()
        else showError(R.string.cannot_read_local_data)
    }

    private fun getProductList() = viewModelScope.launch {
        showLoading()
        when (val result = productRepo.getProductList()) {
            is Result.Error -> result.error.message.actionExceptionMsg(
                offline = {
                    getLocalList()
                },
                unauthorised = { Unit },
                error = {
                    showError(R.string.error_retrieve_data)
                }
            )

            is Result.Value -> {
                productListState.value = result.value.asReversed()
            }
        }
        hideLoading()
    }

    private fun updateCategorySelection(categoryId: Int) = viewModelScope.launch {
        val categorySelects = mutableListOf<CategorySelect>()
        categoryListState.value?.forEach { menu ->
            categorySelects.add(
                CategorySelect(
                    pos = categoryListState.value?.indexOf(menu) ?: -1,
                    isSelect = menu.id == categoryId
                )
            )
        }
        updateCategoryMenuSelectAttempt.value = categorySelects
    }

    private fun onCategoryMenuListClick(categoryId: Int) = viewModelScope.launch {
        updateCategorySelection(categoryId)
        delay(300)
        when (categoryId) {
            MENU_BRAND -> navigateToBrandListAttempt.value = Event(Unit)
        }
    }
}