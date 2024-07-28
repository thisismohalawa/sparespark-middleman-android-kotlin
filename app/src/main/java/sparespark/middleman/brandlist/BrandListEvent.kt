package sparespark.middleman.brandlist

sealed class BrandListEvent {
    data object GetBrands : BrandListEvent()
    data class OnBrandItemClick(val position: Int) : BrandListEvent()
    data class OnSwitchItemUpdated(val position: Int, val isChecked: Boolean) : BrandListEvent()
}
