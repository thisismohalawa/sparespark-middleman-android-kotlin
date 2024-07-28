package sparespark.middleman.model

data class RemoteProduct(
    val creationDate: String? = "",
    val title: String? = "ProductX",
    val des: String? = "",
    val imgUrl: String? = "",
    val brandId: String? = "",
    val price: Double? = 0.0,
    val activated: Boolean? = false,
    val colors: ArrayList<IColor>? = null,
    val related: ArrayList<IRelated>? = null,
)
