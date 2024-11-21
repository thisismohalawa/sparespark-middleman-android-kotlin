package sparespark.middleman.productlist

sealed class ProductListEvent {
    data object OnViewStart : ProductListEvent()
    data class OnMenuCategoryClicked(val categoryId: Int) : ProductListEvent()
    data class OnProductItemClick(val position: Int) : ProductListEvent()
}
