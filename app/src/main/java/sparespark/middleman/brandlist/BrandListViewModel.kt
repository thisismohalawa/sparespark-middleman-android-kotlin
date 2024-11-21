package sparespark.middleman.brandlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Event
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.data.repository.BrandRepository

class BrandListViewModel(
    private val brandRepo: BrandRepository
) : BaseViewModel<BrandListEvent>() {

    private val brandListState = MutableLiveData<List<Brand>>()
    val brandsList: LiveData<List<Brand>> get() = brandListState

    private val editBrandState = MutableLiveData<Event<String>>()
    val editBrand: LiveData<Event<String>> = editBrandState

    override fun handleEvent(event: BrandListEvent) {
        when (event) {
            is BrandListEvent.GetBrands -> getBrandList()
            is BrandListEvent.OnSwitchItemUpdated -> startBrandListener(
                event.isChecked
            )
        }
    }

    private fun getLocalList() = viewModelScope.launch {
        val result = brandRepo.getBrands(localOnly = Unit)
        if (result is Result.Value) brandListState.value = result.value.asReversed()
        else showError(R.string.cannot_read_local_data)
    }

    private fun getBrandList() = viewModelScope.launch {
        showLoading()
        when (val result = brandRepo.getBrands()) {
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
                brandListState.value = result.value.asReversed()
            }
        }
        hideLoading()
    }

    private fun startBrandListener(checked: Boolean) {
        if (checked)
            showError(R.string.brand_service_on)
        else
            showError(R.string.brand_service_off)

    }
}