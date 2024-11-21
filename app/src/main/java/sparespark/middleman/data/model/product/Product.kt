package sparespark.middleman.data.model.product

data class Product(
    var creationDate: String,
    val title: String,
    val des: String,
    val imgUrl: String,
    val brandId: String,
    val price: Double,
    val rating: Int,
    val activated: Boolean,
    val colors: ArrayList<RelatedColor>,
    val related: ArrayList<RelatedUrl>
)
