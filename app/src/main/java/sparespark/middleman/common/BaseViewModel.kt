package sparespark.middleman.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sparespark.middleman.model.Product

abstract class BaseViewModel<T> : ViewModel() {
    abstract fun handleEvent(event: T)

    internal val supportAttempt = MutableLiveData<Event<String>>()

    private val loadingState = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = loadingState

    private val errorState = MutableLiveData<UIResource>()
    val error: LiveData<UIResource> get() = errorState

    private val exploreProductState = MutableLiveData<Event<Product>>()
    val exploreProduct: LiveData<Event<Product>> = exploreProductState

    protected fun showLoading() {
        loadingState.value = true
    }

    protected fun hideLoading() {
        loadingState.value = false
    }

    protected fun showError(resMsg: Int) {
        errorState.value = UIResource.StringResource(resMsg)
    }

    protected fun startProductExploring(product: Product?) {
        exploreProductState.value = Event(product ?: ErrorProduct)
    }

    protected fun startSupportContact(msg: String?) {
        supportAttempt.value = Event(msg ?: "")
    }
}
