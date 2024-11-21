package sparespark.middleman.branddetails


sealed class BrandDetailEvent {
    data object OnStartGetBrand : BrandDetailEvent()
}
