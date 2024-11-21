package sparespark.middleman.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import sparespark.middleman.R
import sparespark.middleman.core.NO_INTERNET_CONNECTION
import sparespark.middleman.core.UNAUTHORIZED
import sparespark.middleman.core.wrapper.Event
import sparespark.middleman.data.model.UIResource

abstract class BaseViewModel<T> : ViewModel() {
    abstract fun handleEvent(event: T)

    private val loadingState = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = loadingState

    private val errorState = MutableLiveData<UIResource>()
    val error: LiveData<UIResource> get() = errorState


    protected fun showLoading() {
        loadingState.value = true
    }

    protected fun hideLoading() {
        loadingState.value = false
    }

    private fun showUnauthorised() {
        errorState.value = UIResource.StringResource(R.string.unauthorized)
    }

    private fun showNoConnection() {
        errorState.value = UIResource.StringResource(R.string.error_no_internet)
    }

    protected fun showError(resMsg: Int) {
        errorState.value = UIResource.StringResource(resMsg)
    }

    protected fun String?.checkExceptionMsg(
        error: (() -> Unit)? = null
    ) {
        when (this) {
            NO_INTERNET_CONNECTION -> showNoConnection()
            UNAUTHORIZED -> showUnauthorised()
            else -> error?.invoke()
        }
    }

    protected fun String?.actionExceptionMsg(
        offline: (() -> Unit)? = null,
        unauthorised: (() -> Unit)? = null,
        error: (() -> Unit)? = null
    ) {
        when (this) {
            NO_INTERNET_CONNECTION -> offline?.invoke()
            UNAUTHORIZED -> unauthorised?.invoke()
            else -> error?.invoke()
        }
    }
}
