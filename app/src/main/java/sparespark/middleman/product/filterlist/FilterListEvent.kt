package sparespark.middleman.product.filterlist

import sparespark.middleman.model.Product

sealed class FilterListEvent {
    data object GetProducts : FilterListEvent()
    data class OnProductClicked(val product: Product) : FilterListEvent()
    data object OnSupportButtonClick : FilterListEvent()
}
