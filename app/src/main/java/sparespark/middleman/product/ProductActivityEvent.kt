package sparespark.middleman.product

interface ProductActivityInteract {
    fun updateLoadingView(isLoading: Boolean)
    fun showError(error: String? = "")
    fun actionFinish()
    fun launchWhatsApp(
        dTitle: String, dPositiveTitle: String,
        wContent: String
    )
}

sealed class ProductActivityEvent {
    data object OnStartCheckUser : ProductActivityEvent()
}