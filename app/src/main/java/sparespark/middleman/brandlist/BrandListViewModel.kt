package sparespark.middleman.brandlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.common.BaseViewModel
import sparespark.middleman.common.Event
import sparespark.middleman.common.NO_INTERNET_CONNECTION
import sparespark.middleman.common.Result
import sparespark.middleman.model.Brand
import sparespark.middleman.model.repository.BrandRepository

class BrandListViewModel(
    private val brandRepo: BrandRepository
) : BaseViewModel<BrandListEvent>() {

    private val brandListState = MutableLiveData<List<Brand>>()
    val brandsList: LiveData<List<Brand>> get() = brandListState

    private val exploreBrandState = MutableLiveData<Event<String>>()
    val exploreBrand: LiveData<Event<String>> = exploreBrandState

    override fun handleEvent(event: BrandListEvent) {
        when (event) {
            is BrandListEvent.GetBrands -> getBrands()
            is BrandListEvent.OnBrandItemClick -> startBrandExplore(event.position)
            is BrandListEvent.OnSwitchItemUpdated -> startBrandListener(
                event.position,
                event.isChecked
            )
        }
    }

    private fun getBrands() = viewModelScope.launch {
        showLoading()
        when (val result = brandRepo.getBrands()) {
            is Result.Error -> when (result.error.message) {
                NO_INTERNET_CONNECTION -> {
                    showError(R.string.no_internet)
                    getLocalBrands()
                }

                else -> showError(R.string.error_load_brands)
            }

            is Result.Value -> brandListState.value = result.value
        }
        hideLoading()
    }

    private suspend fun getLocalBrands() {
        when (val result = brandRepo.getLocalBrands()) {
            is Result.Error -> showError(R.string.can_not_read_local_data)
            is Result.Value -> brandListState.value = result.value
        }
    }

    private fun startBrandExplore(position: Int) {
        exploreBrandState.value =
            Event(brandListState.value?.get(position)?.creationDate.toString())
    }

    private fun startBrandListener(position: Int, checked: Boolean) {
        if (checked)
            showError(R.string.brand_service_on)
        else showError(R.string.brand_service_off)
    }
}