package sparespark.middleman.data.model.product

data class RemoteProduct(
    val creationDate: String? = "",
    val title: String? = "",
    val des: String? = "",
    val imgUrl: String? = "",
    val brandId: String? = "",
    val price: Double? = 0.0,
    val rating: Int? = 0,
    val activated: Boolean? = false,
    val colors: ArrayList<RelatedColor>? = null,
    val related: ArrayList<RelatedUrl>? = null
)
