package sparespark.middleman.productdetails


sealed class ProductDetailEvent {
    data object OnStartGetProduct : ProductDetailEvent()
    data object OnCartTextClick : ProductDetailEvent()
    data class OnRelatedItemClick(val position: Int) : ProductDetailEvent()
    data class OnColorItemClick(val position: Int) : ProductDetailEvent()
}
