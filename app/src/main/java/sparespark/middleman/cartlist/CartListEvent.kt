package sparespark.middleman.cartlist

sealed class CartListEvent {
    data object GetCartProducts : CartListEvent()
    data class OnClearItemClick(val position: Int) : CartListEvent()
    data class OnImgItemClick(val position: Int) : CartListEvent()
    data object OnBackButtonPressed : CartListEvent()
    data object OnSupportTextClick : CartListEvent()
    data object OnBuyButtonClick : CartListEvent()
}
