package sparespark.middleman.product.productlist

import sparespark.middleman.model.Product

sealed class ProductListEvent {
    data object GetProducts : ProductListEvent()
    data class OnProductClicked(val product: Product) : ProductListEvent()
}
