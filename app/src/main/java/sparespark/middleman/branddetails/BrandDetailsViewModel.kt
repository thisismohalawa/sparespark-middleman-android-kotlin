package sparespark.middleman.branddetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.data.repository.BrandRepository

class BrandDetailsViewModel(
    private val brandRepo: BrandRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<BrandDetailEvent>() {

    private val navBrandId: String? = savedStateHandle["brandId"]

    private val brandState = MutableLiveData<Brand>()
    val brand: LiveData<Brand> get() = brandState


    override fun handleEvent(event: BrandDetailEvent) {
        when (event) {
            is BrandDetailEvent.OnStartGetBrand -> {
                navBrandId?.let { brandId ->
                    getBrand(brandId)
                }
            }
        }
    }

    private fun getBrand(brandId: String) = viewModelScope.launch {
        showLoading()
        when (val result = brandRepo.getBrandById(brandId)) {
            is Result.Error -> result.error.message.checkExceptionMsg(error = {
                showError(R.string.error_retrieve_data)
            })

            is Result.Value -> {
                brandState.value = result.value
            }
        }
        hideLoading()
    }
}