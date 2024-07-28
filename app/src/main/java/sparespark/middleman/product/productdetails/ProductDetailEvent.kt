package sparespark.middleman.product.productdetails


sealed class ProductDetailEvent {
    data object OnStartGetProduct : ProductDetailEvent()
    data object GetBrandName : ProductDetailEvent()
    data object OnCartTextClick : ProductDetailEvent()
    data class OnRelatedItemClick(val position: Int) : ProductDetailEvent()
    data class OnColorItemClick(val position: Int) : ProductDetailEvent()
}
